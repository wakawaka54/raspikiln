package org.raspikiln.kiln.sensors

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation

data class TemperatureMeasurement(
    val metric: String,
    val temperature: Temperature
) {
    operator fun plus(another: TemperatureMeasurement): TemperatureMeasurement {
        require(metric == another.metric) { "Temperatures cannot be summed across metrics [$metric] [${another.metric}]" }
        return copy(
            metric = metric,
            temperature = temperature + another.temperature
        )
    }

    operator fun div(amount: Int): TemperatureMeasurement = div(amount.toDouble())

    operator fun div(amount: Double): TemperatureMeasurement =
        copy(
            metric = metric,
            temperature = temperature / amount
        )
}