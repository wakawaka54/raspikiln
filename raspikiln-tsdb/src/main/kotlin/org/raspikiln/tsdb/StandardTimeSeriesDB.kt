package org.raspikiln.tsdb

import org.raspikiln.tsdb.partitions.*
import org.raspikiln.tsdb.partitions.writers.DiskPartitionWriter
import org.raspikiln.tsdb.utils.TimestampRange
import org.raspikiln.tsdb.utils.makeDirectory
import java.io.File
import java.time.Instant
import java.util.concurrent.BlockingDeque
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration

class StandardTimeSeriesDB(private val options: Options) : TimesSeriesDB {
    private val root = options.directory.makeDirectory()

    private val head = AtomicReference(newMemoryPartition(startingTimestamp = Instant.now()))
    private val diskPartitions = DiskPartitionCollection(initial = root.readDiskPartitions(), deleteDelay = options.deleteDelayTime)

    override fun write(measurement: List<Measurement>) {
        head().write(measurement)
        purgeExpired()
    }

    override fun query(metricName: MetricIdentifier, timestampRange: TimestampRange) = sequence {
        yieldAll(diskPartitions.query(metricName, timestampRange))
        yieldAll(head().query(metricName, timestampRange))
    }

    override fun purgeExpired() {
        head()
        diskPartitions.purgeExpired()
    }

    override fun purgeAll() {
        head.set(newMemoryPartition(startingTimestamp = Instant.now()))
        diskPartitions.purgeAll()
    }

    private fun head() =
        head.updateAndGet { current ->
            if (!current.active()) {
                current.writeToDisk()
                newMemoryPartition(startingTimestamp = current.timestampRange().endInclusive)
            } else {
                current
            }
        }

    private fun MemoryPartition.writeToDisk() {
        if (isNotEmpty()) {
            diskPartitions.add(
                newDiskPartition(DiskPartitionWriter(root).apply { writeTo(this) }.close())
            )
        }
    }

    private fun newMemoryPartition(startingTimestamp: Instant) =
        MemoryPartition(options = MemoryPartition.Options(
            startingTimestamp = startingTimestamp,
            partitionDuration = options.partitionDuration,
            partitionCapacity = options.partitionCapacity
        ))

    private fun newDiskPartition(directory: DiskPartitionDirectory) =
        DiskPartition(
            directory = directory,
            options = DiskPartition.Options(
                retention = options.retention
            )
        )

    private fun File.readDiskPartitions(): List<DiskPartition> =
        files()
            .mapNotNull { DiskPartitionDirectory.maybePartition(it) }
            .map { partitionDir -> newDiskPartition(partitionDir) }
            .toList()

    data class Options(
        val directory: File,
        val partitionDuration: Duration,
        val partitionCapacity: Int,
        val retention: Duration,
        val deleteDelayTime: Duration
    )
}

private fun File.files() = listFiles() ?: emptyArray()
