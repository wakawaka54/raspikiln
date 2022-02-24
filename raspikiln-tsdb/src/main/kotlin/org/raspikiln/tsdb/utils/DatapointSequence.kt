package org.raspikiln.tsdb.utils

import org.raspikiln.tsdb.Datapoint
import org.raspikiln.tsdb.encoding.MsgPackDecoder
import org.raspikiln.tsdb.encoding.sequence
import java.nio.channels.ReadableByteChannel
import java.time.Instant
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicInteger

/**
 * Sorted datapoint sequence.
 */
sealed interface DatapointSequence {
    fun timestampHead(): Instant? = sequence().lastOrNull()?.timestamp
    fun sequence(): Sequence<Datapoint>
    fun sequence(range: TimestampRange) =
        sequence().filter { range.contains(it.timestamp) }
}

class DatapointList(private val list: BlockingDeque<Datapoint> = LinkedBlockingDeque())
    : DatapointSequence, Iterable<Datapoint> {

    override fun iterator(): Iterator<Datapoint> = sequence().iterator()

    override fun sequence(): Sequence<Datapoint> = list.asSequence()

     fun add(datapoint: Datapoint) {
        require((timestampHead() ?: datapoint.timestamp) <= datapoint.timestamp) {
            "Datapoint added out of order, not supported."
        }

        list.addLast(datapoint)
    }
}

class ByteChannelDatapointSequence(
    private val timestampRange: TimestampRange,
    private val length: Int,
    private val channelFn: () -> ReadableByteChannel
) : DatapointSequence {
    override fun timestampHead(): Instant = timestampRange.endInclusive

    override fun sequence(): Sequence<Datapoint> =
        MsgPackDecoder(channelFn(), length).sequence()
}