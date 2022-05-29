package org.raspikiln.kiln.initialization

import com.google.common.util.concurrent.Service
import org.raspikiln.kiln.services.KilnServiceManager
import org.raspikiln.kiln.services.KilnServiceRegistry

class KilnInitializationBuilder(
    private val serviceRegistry: KilnServiceRegistry = KilnServiceRegistry()
) {
    fun registerService(service: Service) {
        serviceRegistry.register(service)
    }

    fun build() =
        KilnInitialization(
            serviceManager = KilnServiceManager(serviceRegistry)
        )
}