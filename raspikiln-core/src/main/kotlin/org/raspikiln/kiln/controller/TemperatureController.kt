package org.raspikiln.kiln.controller

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.zones.KilnZoneName
import kotlin.time.Duration

interface TemperatureController {
    fun name(): String
    fun dutyCycle(): Duration
    fun zones(): Set<KilnZoneName>
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