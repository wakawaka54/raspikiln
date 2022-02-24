package org.raspikiln.tsdb.partitions

import org.raspikiln.tsdb.*
import org.raspikiln.tsdb.partitions.writers.PartitionWriter
import org.raspikiln.tsdb.utils.*
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.ranges.rangeTo
import kotlin.time.Duration

class MemoryPartition(private val options: Options) : MutablePartition {
    private val metrics = ConcurrentHashMap<MetricIdentifier, MetricData>()
    private val numberOfPoints = AtomicInteger(0)
    private val minimumTimestamp = options.startingTimestamp
    private val maximumTimestamp = AtomicReference(minimumTimestamp)

    override fun active(): Boolean =
        (Instant.now() - timestampRange().start) < options.partitionDuration
                && numberOfPoints.get() < options.partitionCapacity

    override fun timestampRange(): TimestampRange = minimumTimestamp..maximumTimestamp

    override fun expired(): Boolean = false

    override fun size(): Int = numberOfPoints.get()

    override fun size(metricName: MetricIdentifier): Int = metrics[metricName]?.size() ?: 0

    override fun write(measurements: List<Measurement>) {
        measurements.forEach { measurement ->
            require(measurement.datapoint.timestamp >= minimumTimestamp) {
                "Measurement point must be within the partition time range."
            }

            metrics.computeIfAbsent(measurement.metric) { MetricData() }.write(measurement)
        }

        numberOfPoints.getAndAdd(measurements.size)
    }

    fun <T> writeTo(encoder: PartitionWriter<T>) {
        metrics.forEach { (key, value) ->
            encoder.nextSeries(this, key).use { encoder ->
                value.points.sequence().forEach { point -> encoder.write(point) }
            }
        }
    }

    override fun query(
        metricIdentifier: MetricIdentifier,
        range: TimestampRange
    ): Sequence<Datapoint> =
        if (timestampRange().intersects(range)) {
            metrics[metricIdentifier]?.sequence(range) ?: emptySequence()
        } else {
            emptySequence()
        }

    override fun delete() {
        metrics.clear()
        numberOfPoints.set(0)
        maximumTimestamp.set(minimumTimestamp)
    }

    override fun close() {
        // do nothing
    }

    private fun MetricData.write(measurement: Measurement) {
        add(measurement.datapoint)
        maximumTimestamp.set(points.timestampHead() ?: minimumTimestamp)
    }

    data class Options(
        val startingTimestamp: Instant,
        val partitionDuration: Duration,
        val partitionCapacity: Int
    )
}

private data class MetricData(
    val points: DatapointList = DatapointList()
) {
    private val size = AtomicInteger(0)

    fun add(datapoint: Datapoint) {
        size.getAndIncrement()
        points.add(datapoint)
    }

    fun sequence(timestampRange: TimestampRange) = points.sequence(timestampRange)

    fun size() = size.get()
}