package org.raspikiln.kiln.metrics.reporters

import org.raspikiln.core.services.scheduledService
import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.kiln.metrics.KilnMetricsRegistry
import org.raspikiln.kiln.metrics.KilnMetricsReporter
import org.raspikiln.kiln.module.KilnModuleSpec
import org.raspikiln.tsdb.Datapoint
import org.raspikiln.tsdb.Measurement
import org.raspikiln.tsdb.MetricIdentifier
import org.raspikiln.tsdb.TimesSeriesDB
import kotlin.time.Duration.Companion.seconds

/**
 * Register a historical time series metrics reporter.
 */
fun KilnModuleSpec.historicalTimeSeriesMetricsReporter(timesSeriesDB: TimesSeriesDB) =
    reporter(HistoricalTimeSeriesMetricsReporter(timesSeriesDB))

class HistoricalTimeSeriesMetricsReporter(private val timesSeriesDB: TimesSeriesDB) : KilnMetricsReporter {

    override fun initialize(builder: KilnInitializationBuilder) {
        val service = scheduledService(name = "HistoricalTimeSeriesReporter", period = 2.seconds) {
            reportFrom(builder.metrics())
        }

        builder.registerService(service)
    }

    private fun reportFrom(registry: KilnMetricsRegistry) {
        val report = registry.report()

        report.counters.reportAll { name, value -> measurement(name, value) }
        report.gauges.reportAll { name, value -> measurement(name, value) }
    }

    private fun <K, V> Map<K, V>.reportAll(mappingFn: (K, V) -> Measurement) =
        entries.chunked(20).forEach {
                chunked -> timesSeriesDB.write(chunked.map { (key, value) -> mappingFn(key, value) })
        }

    private fun measurement(name: String, value: Double) =
        Measurement(
            metric = MetricIdentifier(name = name),
            datapoint = Datapoint(value = value)
        )
}