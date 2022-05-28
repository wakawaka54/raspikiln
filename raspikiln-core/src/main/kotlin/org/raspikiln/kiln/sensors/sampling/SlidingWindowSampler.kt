package org.raspikiln.kiln.sensors.sampling

import org.raspikiln.tsdb.utils.minus
import java.time.Clock
import java.time.Instant
import java.util.LinkedList
import kotlin.time.Duration

/**
 * Sliding window type sampler.
 */
class SlidingWindowSampler<T>(
    private val clock: Clock = Clock.systemUTC(),
    private val window: Duration,
    private val capacity: Int,
) {
    private val samples: SampleList<T> = LinkedList<Sample<T>>()

    fun sample(next: T) {
        synchronized(samples) {
            samples
                .addSample(next)
                .purgeStaleSamples()
        }
    }

    fun read(): List<T> =
        synchronized(samples) {
            samples.map { it.data }
        }

    private fun SampleList<T>.addSample(sample: T) = apply {
        add(Sample(clock.instant(), sample))
    }

    private fun SampleList<T>.purgeStaleSamples() = apply {
        val expirationTime = clock.instant() - window
        while (expirationTime > samples.firstOrNull()?.timestamp) {
            samples.removeFirst()
        }

        while (samples.size > capacity) {
            samples.removeFirst()
        }
    }
}

private typealias SampleList<T> = MutableList<Sample<T>>

private data class Sample<T>(
    val timestamp: Instant,
    val data: T
)