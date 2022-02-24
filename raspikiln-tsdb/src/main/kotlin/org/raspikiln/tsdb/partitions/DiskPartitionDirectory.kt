package org.raspikiln.tsdb.partitions

import org.raspikiln.tsdb.MetricIdentifier
import org.raspikiln.tsdb.utils.JsonMapper
import org.raspikiln.tsdb.utils.TimestampRange
import org.raspikiln.tsdb.utils.makeDirectory
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.Instant

class DiskPartitionDirectory(private val directory: File) {
    companion object {
        private const val PARTITION_PREFIX = "p"
        private const val SEPERATOR = "-"
        private const val META_FILENAME = "meta.json"
        private const val DATA_FILENAME = "data"

        fun createNew(root: File, timestampRange: TimestampRange) =
            DiskPartitionDirectory(
                File(root,
                    listOf(
                        PARTITION_PREFIX,
                        timestampRange.start.toEpochMilli(),
                        timestampRange.endInclusive.toEpochMilli()
                    )
                    .joinToString(SEPERATOR)
                )
                .makeDirectory()
            )

        fun maybePartition(directory: File) =
            directory.takeIf { isPartition(it) }?.let { DiskPartitionDirectory(it) }

        fun isPartition(directory: File) =
            directory.name.startsWith(PARTITION_PREFIX + SEPERATOR)
    }

    private val metaFile = File(directory, META_FILENAME)
    private val dataFile = File(directory, DATA_FILENAME)

    fun writeMeta(meta: Meta) {
        JsonMapper.writeValue(metaFile, meta)
    }

    fun writeData(transient: File) {
        Files.move(
            transient.toPath(),
            dataFile.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
    }

    fun metaFile() = metaFile
    fun dataFile() = dataFile

    fun delete() {
        directory.deleteRecursively()
    }
}

data class Meta(
    val minTimestamp: Instant,
    val maxTimestamp: Instant,
    val numberOfDataPoints: Int,
    val metrics: Map<String, DiskMetric>,
    val createdAt: Instant
) {
    fun timeRange() = minTimestamp..maxTimestamp

    data class DiskMetric(
        val name: String,
        val offset: Long,
        val minTimestamp: Instant,
        val maxTimestamp: Instant,
        val numberOfDataPoints: Int
    ) {
        fun metricTimeRange() = minTimestamp..maxTimestamp
    }

    operator fun get(metricIdentifier: MetricIdentifier) = metrics[metricIdentifier.toString()]
}