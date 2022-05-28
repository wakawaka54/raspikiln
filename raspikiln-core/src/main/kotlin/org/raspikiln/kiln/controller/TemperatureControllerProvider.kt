package org.raspikiln.kiln.controller

import org.raspikiln.kiln.config.controllers.ControllerConfig
import org.raspikiln.kiln.controller.algorithm.TupleTerms
import kotlin.time.Duration.Companion.milliseconds

/**
 * Standard temperature controller provider.
 */
class TemperatureControllerProvider {
    fun create(controllerConfig: ControllerConfig) =
        when (controllerConfig) {
            is ControllerConfig.OnOff -> controllerConfig.create()
            is ControllerConfig.PID -> controllerConfig.create()
        }

    private fun ControllerConfig.OnOff.create() =
        OnOffController(
            zones.toSet(),
            OnOffController.Options(
                hysteresis = hysteresis,
                dutyCycle = dutyCycle.milliseconds
            )
        )

    private fun ControllerConfig.PID.create() =
        PidController(
            zones.toSet(),
            PidController.Options(
                dutyCycle = dutyCycle.milliseconds,
                gainTerms = TupleTerms(p = kp, i = ki, d = kd)
            )
        )
}