package org.raspikiln.tsdb.encoding

import org.msgpack.core.MessagePack
import org.raspikiln.tsdb.Datapoint
import org.raspikiln.tsdb.utils.minus
import java.nio.channels.WritableByteChannel
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.DurationUnit

class MsgPackEncoder(channel: WritableByteChannel) : SeriesEncoder {
    private val packer = MessagePack.newDefaultPacker(channel)

    override fun write(datapoint: Datapoint) {
        packer.packLong(datapoint.timestamp.toEpochMilli())
        packer.packDouble(datapoint.value)
    }

    override fun close() {
        packer.flush()
    }
}