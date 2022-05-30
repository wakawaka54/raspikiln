package org.raspikiln.kiln.metrics

import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.tsdb.Datapoint
import org.raspikiln.tsdb.Measurement
import org.raspikiln.tsdb.MetricIdentifier
import org.raspikiln.tsdb.TimesSeriesDB

class HistoricalTimeSeriesMetrics(
    private val registry: KilnMetricsRegistry,
    private val timesSeriesDB: TimesSeriesDB
) : KilnMetricsReporter {

    override fun initialize(builder: KilnInitializationBuilder) {

    }

    override fun emit(name: String, value: Double) {
        timesSeriesDB.write(
            Measurement(
                metric = MetricIdentifier(name),
                datapoint = Datapoint(value = value)
            )
        )
    }
}