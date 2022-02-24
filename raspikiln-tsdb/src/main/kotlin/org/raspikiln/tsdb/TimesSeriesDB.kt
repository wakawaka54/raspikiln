package org.raspikiln.tsdb

import org.raspikiln.tsdb.utils.TimestampRange

interface TimesSeriesDB {

    companion object {
        fun create(options: StandardTimeSeriesDB.Options): TimesSeriesDB =
            StandardTimeSeriesDB(options)
    }

    /**
     * Write measurements varargs.
     */
    fun write(vararg measurements: Measurement) = write(measurements.toList())

    /**
     * Write metric measurements.
     */
    fun write(measurement: List<Measurement>)

    /**
     * Query a metric.
     */
    fun query(metricName: MetricIdentifier, timestampRange: TimestampRange): Sequence<Datapoint>

    /**
     * Purge expired partitions.
     */
    fun purgeExpired()

    /**
     * Completely purge the entire database.
     */
    fun purgeAll()
}