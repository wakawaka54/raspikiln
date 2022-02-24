package org.raspikiln.tsdb.partitions

import org.raspikiln.tsdb.Datapoint
import org.raspikiln.tsdb.Measurement
import org.raspikiln.tsdb.MetricIdentifier
import org.raspikiln.tsdb.utils.TimestampRange
import java.io.Closeable

sealed interface Partition : Closeable {
    fun expired(): Boolean
    fun timestampRange(): TimestampRange
    fun size(): Int
    fun size(metricName: MetricIdentifier): Int
    fun query(metricIdentifier: MetricIdentifier, range: TimestampRange): Sequence<Datapoint>
    fun delete()

    fun isEmpty() = size() == 0
    fun isNotEmpty() = !isEmpty()
}

interface ImmutablePartition : Partition

interface MutablePartition : Partition {
    fun active(): Boolean
    fun write(measurements: List<Measurement>)
}