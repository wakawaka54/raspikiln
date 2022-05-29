package org.raspikiln.kiln.controller

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.switches.SwitchState
import kotlin.time.Duration

interface TemperatureController {
    fun name(): String
    fun dutyCycle(): Duration
    fun locations(): Set<KilnLocation>
    fun reset()
    fun evaluate(input: ControllerInput): ControllerOutput
}

data class ControllerInput(
    val setpoint: Temperature,
    val temperature: Temperature,
    val heaterState: SwitchState,
    val dutyCycle: Duration,
)

data class ControllerOutput(
    val heaterOn: Duration,
    val heaterOff: Duration
)