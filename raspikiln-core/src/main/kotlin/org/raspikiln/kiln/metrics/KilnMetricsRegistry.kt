package org.raspikiln.kiln.metrics

interface KilnMetricsRegistry {
    fun emit(name: String, value: Double)
    fun gauges(): Map<String, Double>
}