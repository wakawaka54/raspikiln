package org.raspikiln.tsdb.partitions.writers

import org.raspikiln.tsdb.MetricIdentifier
import org.raspikiln.tsdb.encoding.SeriesEncoder
import org.raspikiln.tsdb.partitions.Partition

interface PartitionWriter<T> {
    fun nextSeries(partition: Partition, metricName: MetricIdentifier): SeriesEncoder
    fun close(): T?
}