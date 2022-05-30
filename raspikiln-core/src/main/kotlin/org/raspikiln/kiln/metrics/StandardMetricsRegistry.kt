package org.raspikiln.kiln.metrics

import java.util.concurrent.ConcurrentHashMap

class StandardMetricsRegistry : KilnMetricsRegistry {
    private val metrics = ConcurrentHashMap<String, Double>()

    override fun emit(name: String, value: Double) {
        metrics[name] = value
    }

    override fun gauges(): Map<String, Double> = metrics
}