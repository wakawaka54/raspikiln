package org.raspikiln.kiln.metrics

interface KilnMetricsRegistry {
    companion object {
        fun standard() = StandardKilnMetricsRegistry()
    }

    fun counter(name: String, value: Double)
    fun gauge(name: String, value: Double)
    fun report(): RegistryReport
}

data class RegistryReport(
    val counters: Map<String, Double>,
    val gauges: Map<String, Double>
)