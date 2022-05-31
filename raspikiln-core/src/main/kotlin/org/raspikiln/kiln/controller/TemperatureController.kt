package org.raspikiln.kiln.controller

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.initialization.KilnInitializer
import org.raspikiln.kiln.programs.types.TemperatureSetpoint
import org.raspikiln.kiln.state.KilnState
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.switches.SwitchState
import kotlin.time.Duration

interface TemperatureController : KilnInitializer {
    val name: String
    val armingSwitch: Switch?
    val heaterSwitch: Switch

    fun error(): Double
    fun set(setpoint: TemperatureSetpoint)
    fun unset()
}

data class ControllerInput(
    /**
    val setpoint: Temperature,
    val temperature: Temperature,
    val heaterState: SwitchState,
    **/
    val setpoint: Temperature,
    val bridge: KilnBridge,
    val state: KilnState,
    val dutyCycle: Duration,
)

data class ControllerOutput(
    val heaterOn: Duration,
    val heaterOff: Duration
)