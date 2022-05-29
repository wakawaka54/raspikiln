package org.raspikiln.kiln.services

import com.google.common.util.concurrent.Service

class KilnServiceRegistry {
    private val services = mutableListOf<Service>()

    fun register(service: Service) {
        services.add(service)
    }

    fun getAll() = services.toList()
}