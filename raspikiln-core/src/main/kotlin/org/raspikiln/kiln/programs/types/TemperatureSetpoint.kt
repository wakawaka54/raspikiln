package org.raspikiln.kiln.programs.types

import org.raspikiln.core.units.Temperature

sealed interface TemperatureSetpoint {
    object Unset : TemperatureSetpoint {
        override fun maybeTemperature(): Temperature? = null
    }

    data class Value(val temperature: Temperature) : TemperatureSetpoint {
        override fun maybeTemperature(): Temperature = temperature
    }

    fun maybeTemperature(): Temperature?
}