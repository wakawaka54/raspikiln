package org.raspikiln.http.timeseries

import mu.KotlinLogging
import org.raspikiln.tsdb.Datapoint
import org.raspikiln.tsdb.MetricIdentifier
import org.raspikiln.tsdb.TimesSeriesDB
import org.raspikiln.tsdb.utils.TimestampRange
import java.time.Instant
import java.util.*
import kotlin.math.floor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger { }

class TimeSeriesQueryEngine(private val timeSeriesDb: TimesSeriesDB) {
    fun query(query: TimeSeriesQuery): List<QueryDatapoint> {
        val buckets = query.steps().toList()

        query.metricNames.forEach { metricName ->
            timeSeriesDb.queryWithResolution(metricName, query, buckets)
        }

        return buckets.toList()
    }

    private fun TimesSeriesDB.queryWithResolution(
        metricName: String,
        query: TimeSeriesQuery,
        buckets: List<QueryDatapoint>
    ) {
        for (datapoint in query(MetricIdentifier(name = metricName), query.timestampRange)) {
            if (!query.timestampRange.contains(datapoint.timestamp)) {
                logger.warn { "Datapoint was outside of time range, this should not happen." }
                continue
            }

            val bucketIndex = datapoint.bucketIndex(query.start, query.step)
            val bucket = buckets.getOrNull(bucketIndex)

            if (bucket == null) {
                logger.warn { "Query did not contain bucket [$bucketIndex]" }
                continue
            }

            if (bucket.values.containsKey(metricName)) {
                continue
            }

            bucket.values[metricName] = datapoint.value
        }
    }

    private fun Datapoint.bucketIndex(start: Instant, step: Duration): Int {
        val delta = (timestamp.toEpochMilli() - start.toEpochMilli()).toDouble()
        val bucketIndex = delta / step.inWholeMilliseconds.toDouble()
        return floor(bucketIndex).toInt()
    }
}

data class TimeSeriesQuery(

    /**
     * Metric names.
     */
    val metricNames: List<String>,

    /**
     * Start time of the query.
     */
    val start: Instant,

    /**
     * End time of the query.
     */
    val end: Instant,

    /**
     * Step size.
     */
    val step: Duration = 10.seconds,

    /**
     * Number of points to return in the range.
     */
    val resolution: Int = 120
) {
    val timestampRange = start..end
}

data class QueryDatapoint(
    val interval: ClosedRange<Instant>,
    val values: MutableMap<String, Double> = mutableMapOf()
) {
    operator fun get(name: String): Double? = values[name]
}

data class TimeSeriesResult(
    val series: List<String>,
    val values: List<List<Double>>
)

/**
 * Create query steps.
 */
private fun TimeSeriesQuery.steps() =
    generateSequence(start) { previous -> previous.plusMillis(step.inWholeMilliseconds) }
        .zipWithNext { last, next -> QueryDatapoint(last..next.minusNanos(1)) }
        .takeWhile { it.interval.endInclusive <= end }