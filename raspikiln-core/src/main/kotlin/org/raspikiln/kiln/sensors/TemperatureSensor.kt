package org.raspikiln.kiln.sensors

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.zones.KilnZoneName

interface TemperatureSensor : Sensor {
    fun location(): KilnLocation
    fun read(): TemperatureMeasurement
}

data class TemperatureMeasurement(
    val temperature: Temperature
)