package org.raspikiln.kiln.controller

import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.kiln.initialization.KilnInitializer

class KilnControllerRegistry(private val controllers: List<TemperatureController>) : KilnInitializer {

    override fun initialize(builder: KilnInitializationBuilder) {
        controllers.forEach { it.initialize(builder) }
    }

    fun find(name: String) = controllers.find { it.name == name }
    fun requiredFind(name: String) = requireNotNull(find(name)) { "Missing required controller [$name]" }
}