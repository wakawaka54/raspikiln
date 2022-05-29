package org.raspikiln.kiln.sensors

import org.raspikiln.kiln.initialization.KilnInitializationBuilder

interface Sensor {
    fun name(): String

    fun initialize(initializer: KilnInitializationBuilder) {
        // do nothing
    }
}