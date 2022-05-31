package org.raspikiln.kiln.controller

import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.config.controllers.ControllerConfig
import org.raspikiln.kiln.controller.algorithm.TupleTerms
import org.raspikiln.kiln.state.KilnState
import kotlin.time.Duration.Companion.milliseconds

/**
 * Standard temperature controller provider.
 */
class TemperatureControllerFactory(
    private val bridge: KilnBridge,
    private val state: KilnState
) {
    fun create(controllerConfig: ControllerConfig) =
        when (controllerConfig) {
            is ControllerConfig.OnOff -> error("Not allowed.")
            is ControllerConfig.PID -> controllerConfig.create()
        }

    /**
    private fun ControllerConfig.OnOff.create() =
        OnOffController(
            armingSwitch = armingSwitch?.requiredSwitch(),
            heaterSwitch = heaterSwitch.requiredSwitch(),
            OnOffController.Options(
                hysteresis = hysteresis,
                dutyCycle = dutyCycle.milliseconds
            )
        )
    **/

    private fun ControllerConfig.PID.create() =
        PidController(
            name = name,
            armingSwitch = armingSwitch?.requiredSwitch(),
            heaterSwitch = heaterSwitch.requiredSwitch(),
            state = state,
            options = PidController.Options(
                dutyCycle = dutyCycle.milliseconds,
                gainTerms = TupleTerms(p = kp, i = ki, d = kd),
                controlTemperatureMetric = controlTemperature,
                targetTemperatureMetric = targetMetric
            )
        )

    private fun String.requiredSwitch() = bridge.requireSwitch(this)
}