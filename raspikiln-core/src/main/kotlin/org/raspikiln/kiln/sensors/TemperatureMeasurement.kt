package org.raspikiln.kiln.sensors

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation

data class TemperatureMeasurement(
    val location: KilnLocation,
    val temperature: Temperature
) {
    operator fun plus(another: TemperatureMeasurement): TemperatureMeasurement {
        require(location == another.location) { "Temperatures cannot be summed across locations [$location] [${another.location}]" }
        return copy(
            location = location,
            temperature = temperature + another.temperature
        )
    }

    operator fun div(amount: Int): TemperatureMeasurement = div(amount.toDouble())

    operator fun div(amount: Double): TemperatureMeasurement =
        copy(
            location = location,
            temperature = temperature / amount
        )
}