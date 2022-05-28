package org.raspikiln.kiln.legacysensors

import org.raspikiln.core.units.Temperature
import org.raspikiln.core.units.average
import org.raspikiln.kiln.common.KilnLocation

interface TemperatureSensor : Sensor {
    fun location(): KilnLocation
    fun read(): TemperatureMeasurement
}

data class TemperatureMeasurement(
    val temperature: Temperature
)

fun Collection<TemperatureSensor>.average() = map { it.read().temperature }.average()