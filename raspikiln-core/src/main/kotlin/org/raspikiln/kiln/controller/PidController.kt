package org.raspikiln.kiln.controller

import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.controller.algorithm.ProportionalIntegralDerivative
import org.raspikiln.kiln.controller.algorithm.TupleTerms
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * PID Controller.
 */
class PidController(
    private val locations: Set<KilnLocation>,
    private val options: Options
) : TemperatureController {
    private val pid = ProportionalIntegralDerivative(
        gainTerms = options.gainTerms,
        outputBounds = 0.0..50.0,
        outputScale = 50.0
    )

    override fun name(): String = "PID"

    override fun locations(): Set<KilnLocation> = locations

    override fun dutyCycle(): Duration = options.dutyCycle

    override fun reset() {
        pid.reset()
    }

    override fun evaluate(input: ControllerInput): ControllerOutput {
        val computation = pid.compute(
            duration = input.dutyCycle,
            actual = input.temperature.celsius().value,
            target = input.setpoint.celsius().value
        )

        val heaterOn = input.dutyCycleMilliseconds() * computation.output
        val heaterOff = input.dutyCycleMilliseconds() * (1 - computation.output)

        return ControllerOutput(
            heaterOn = heaterOn.milliseconds,
            heaterOff = heaterOff.milliseconds
        )
    }

    private fun ControllerInput.dutyCycleMilliseconds() = dutyCycle.inWholeMilliseconds.toDouble()

    data class Options(
        val dutyCycle: Duration,
        val gainTerms: TupleTerms
    )
}