package org.raspikiln.tsdb.partitions

import org.raspikiln.tsdb.*
import org.raspikiln.tsdb.encoding.MsgPackDecoder
import org.raspikiln.tsdb.encoding.sequence
import org.raspikiln.tsdb.utils.*
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.time.Instant
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration

class DiskPartition(
    private val directory: DiskPartitionDirectory,
    private val options: Options
) : ImmutablePartition {
    private val opened = AtomicBoolean(true)
    private val meta = JsonMapper.readValue(directory.metaFile(), Meta::class)
    private val data = RandomAccessFile(directory.dataFile(), "r")

    override fun expired(): Boolean = meta.timeRange().endInclusive < Instant.now() - options.retention

    override fun timestampRange(): TimestampRange = meta.timeRange()

    override fun size(): Int = meta.numberOfDataPoints

    override fun size(metricName: MetricIdentifier): Int = meta.metrics[metricName.toString()]?.numberOfDataPoints ?: 0

    override fun query(
        metricIdentifier: MetricIdentifier,
        range: TimestampRange
    ): Sequence<Datapoint> =
        requireOpen {
            if (timestampRange().intersects(range)) {
                meta[metricIdentifier]?.query(range) ?: emptySequence()
            } else {
                emptySequence()
            }
        }

    override fun close() {
        opened.set(false)
        data.close()
    }

    override fun delete() {
        close()
        directory.delete()
    }

    private fun Meta.DiskMetric.query(timestampRange: TimestampRange): Sequence<Datapoint> =
        ByteChannelDatapointSequence(
            timestampRange.coerce(metricTimeRange()),
            length = numberOfDataPoints
        ) { data.channel.position(offset) }.sequence(timestampRange)

    private fun <T> requireOpen(block: () -> T) =
        if (opened.get()) {
            block()
        } else {
            error("Disk partition has been closed.")
        }

    data class Options(
        val retention: Duration
    )
}