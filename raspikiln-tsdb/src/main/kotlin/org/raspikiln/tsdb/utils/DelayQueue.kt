package org.raspikiln.tsdb.utils

import java.time.Instant
import java.util.concurrent.LinkedBlockingQueue
import kotlin.time.Duration

class DelayQueue<T>(private val delayAmount: Duration) {
    private val queue = LinkedBlockingQueue<QueueEntry<T>>()

    fun add(entry: T) =
        queue.add(QueueEntry(delayUntil = Instant.now() + delayAmount, entry = entry))

    fun nextSequence() = generateSequence { maybeNext() }

    fun maybeNext(): T? {
        synchronized(queue) {
            val next = queue.peek() ?: return null
            if (next.delayUntil <= Instant.now()) {
                return queue.remove().entry
            }
        }

        return null
    }
}

private data class QueueEntry<T>(
    val delayUntil: Instant,
    val entry: T
)