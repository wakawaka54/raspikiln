package org.raspikiln.tsdb.partitions.writers

import org.raspikiln.tsdb.MetricIdentifier
import org.raspikiln.tsdb.encoding.MsgPackEncoder
import org.raspikiln.tsdb.encoding.SeriesEncoder
import org.raspikiln.tsdb.partitions.DiskPartition
import org.raspikiln.tsdb.partitions.DiskPartitionDirectory
import org.raspikiln.tsdb.partitions.Meta
import org.raspikiln.tsdb.partitions.Partition
import org.raspikiln.tsdb.utils.JsonMapper
import java.io.File
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.time.Instant

class DiskPartitionWriter(
    private val root: File
) : PartitionWriter<DiskPartitionDirectory> {
    private val dataTransient = File.createTempFile("data", "transient")

    private val dataChannel = FileChannel.open(dataTransient.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE)
    private val metrics = mutableListOf<Meta.DiskMetric>()

    override fun nextSeries(partition: Partition, metricName: MetricIdentifier): SeriesEncoder {
        metrics += Meta.DiskMetric(
            name = metricName.toString(),
            offset = dataChannel.position(),
            minTimestamp = partition.timestampRange().start,
            maxTimestamp = partition.timestampRange().endInclusive,
            numberOfDataPoints = partition.size(metricName)
        )

        return MsgPackEncoder(dataChannel)
    }

    override fun close(): DiskPartitionDirectory {
        val minTimestamp = metrics.minOf { it.minTimestamp }
        val maxTimestamp = metrics.maxOf { it.maxTimestamp }
        val datapointSize = metrics.sumOf { it.numberOfDataPoints }

        return DiskPartitionDirectory.createNew(root, minTimestamp..maxTimestamp).apply {
            writeMeta(
                Meta(
                    minTimestamp = minTimestamp,
                    maxTimestamp = maxTimestamp,
                    numberOfDataPoints = datapointSize,
                    metrics = metrics.associateBy { it.name },
                    createdAt = Instant.now()
                )
            )

            writeData(dataTransient)
        }
    }
}