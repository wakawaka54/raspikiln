package org.raspikiln.kiln.services

import com.google.common.util.concurrent.ServiceManager
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

class KilnServiceManager(registry: KilnServiceRegistry) {

    private val manager = ServiceManager(registry.getAll())

    fun startAll() {
        logger.info { "Starting all kiln services." }
        manager.startAsync()

        manager.awaitHealthy()
    }

    fun stopAll() {
        logger.info { "Stopping all kiln services." }

        manager.stopAsync()
    }
}