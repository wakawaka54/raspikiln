package org.raspikiln.tsdb.encoding

import org.msgpack.core.MessagePack
import org.raspikiln.tsdb.Datapoint
import java.io.InputStream
import java.nio.channels.ReadableByteChannel
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 * Decodes a stream of datapoints.
 */
class MsgPackDecoder(
    channel: ReadableByteChannel,
    length: Int
) : SeriesDecoder {
    private val unpacker = MessagePack.newDefaultUnpacker(channel)
    private val remaining = AtomicInteger(length)

    override fun readNext(): Datapoint? =
        if (unpacker.hasNext() && remaining.getAndDecrement() > 0) {
            Datapoint(
                timestamp = Instant.ofEpochMilli(unpacker.unpackLong()),
                value = unpacker.unpackDouble()
            )
        } else {
            null
        }
}