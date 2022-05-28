package org.raspikiln.kiln.controller

import org.raspikiln.core.units.Percentage
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.zones.KilnZoneName
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class OnOffController(
    private val zones: Set<KilnZoneName>,
    private val options: Options
) : TemperatureController {
    override fun name(): String = "OnOff"

    override fun zones(): Set<KilnZoneName> = zones

    override fun dutyCycle(): Duration = options.dutyCycle

    override fun reset() { }

    override fun evaluate(input: ControllerInput): ControllerOutput =
        if (input.temperature > input.setpointOff()) {
            input.heaterOff()
        } else if (input.temperature < input.setpointOn()) {
            input.heaterOn()
        } else {
            input.heaterSame()
        }

    private fun ControllerInput.heaterOff() =
        ControllerOutput(
            heaterOn = 0.seconds,
            heaterOff = dutyCycle
        )

    private fun ControllerInput.heaterOn() =
        ControllerOutput(
            heaterOn = dutyCycle,
            heaterOff = 0.seconds
        )

    private fun ControllerInput.heaterSame() =
        when (heaterState) {
            SwitchState.On -> heaterOn()
            SwitchState.Off -> heaterOff()

        }
    private fun ControllerInput.setpointOff() = setpoint

    private fun ControllerInput.setpointOn() = setpoint.percentage(1.0 - options.hysteresis)

    data class Options(
        /**
         * Hystersis percentage, if the setpoint is set to 100C and this is set to 0.2%,
         * then the controller will turn off at 100C but turn back on when the temperature is below
         * 99.2C.
         */
        val hysteresis: Percentage,

        /**
         * Duty cycle of the controller.
         */
        val dutyCycle: Duration
    )
}