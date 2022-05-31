package org.raspikiln.kiln.initialization

import com.google.common.util.concurrent.Service
import org.raspikiln.kiln.metrics.KilnMetricsRegistry
import org.raspikiln.kiln.services.KilnServiceManager
import org.raspikiln.kiln.services.KilnServiceRegistry

class KilnInitializationBuilder(
    private val metricsRegistry: KilnMetricsRegistry,
    private val serviceRegistry: KilnServiceRegistry = KilnServiceRegistry()
) {
    fun metrics() = metricsRegistry

    fun registerService(service: Service) {
        serviceRegistry.register(service)
    }

    fun build() =
        KilnInitialization(
            metricsRegistry = metricsRegistry,
            serviceManager = KilnServiceManager(serviceRegistry)
        )
}