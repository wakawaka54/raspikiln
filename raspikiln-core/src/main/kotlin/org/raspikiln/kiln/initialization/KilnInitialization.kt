package org.raspikiln.kiln.initialization

import org.raspikiln.kiln.metrics.KilnMetricsRegistry
import org.raspikiln.kiln.services.KilnServiceManager

class KilnInitialization(
    private val metricsRegistry: KilnMetricsRegistry,
    private val serviceManager: KilnServiceManager
) {
    fun start() = apply {
        serviceManager.startAll()
    }

    fun stop() = apply {
        serviceManager.stopAll()
    }
}