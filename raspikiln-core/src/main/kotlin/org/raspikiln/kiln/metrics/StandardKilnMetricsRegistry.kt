package org.raspikiln.kiln.metrics

import java.util.concurrent.ConcurrentHashMap

/**
 * Standard implementation of the kiln metrics registry.
 */
class StandardKilnMetricsRegistry : KilnMetricsRegistry {
    private val counters = ConcurrentHashMap<String, Double>()
    private val gauges = ConcurrentHashMap<String, Double>()

    override fun counter(name: String, value: Double) {
        counters.compute(name) { _, last -> last?.plus(value) ?: value }
    }

    override fun gauge(name: String, value: Double) {
        gauges[name] = value
    }

    override fun report(): RegistryReport = RegistryReport(counters = counters, gauges = gauges)
}