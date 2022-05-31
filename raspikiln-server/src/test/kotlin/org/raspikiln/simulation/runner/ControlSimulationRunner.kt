package org.raspikiln.simulation.runner

import org.raspikiln.core.units.Temperature
import org.raspikiln.simulation.ControlSimulation
import org.raspikiln.simulation.SimulationStep
import java.io.File
import java.time.Instant
import kotlin.time.Duration.Companion.minutes

class ControlSimulationRunner(
    private val resultDirectory: File
) {
    fun run(input: SimulationRunnerOptions): SimulationResults {
        /**
        val simulation = ControlSimulation(
            ControlSimulation.Options(
                startTime = Instant.parse("2022-01-01T00:00:00Z"),
                setpoint = Temperature.Celsius(input.steps.first().setpoint),
                gainTerms = input.gainTerms
            )
        )

        val states = input.steps
            .flatMap { simulation.simulate(it) }
            .map { it.toSimulationStepState() }
            .sparse(size = 480)

        return SimulationResults.create(resultDirectory).apply {
            writeData(
                SimulationResultData(
                    options = input,
                    states = states
                )
            )
        }
        **/

        return SimulationResults(resultDirectory)
    }

    /**
    private fun ControlSimulation.simulate(
        step: SimulationRunnerOptions.StepOptions
    ): List<SimulationStep> {
        setpoint(Temperature.Celsius(step.setpoint))
        return sequence(step.duration.minutes).toList()
    }

    private fun SimulationStep.toSimulationStepState() =
        SimulationStepState(
            timestamp = state().timestamp,
            setpoint = state().setpoint?.celsius()?.value ?: 0.0,
            temperature = state().temperature.celsius().value
        )

    **/
}

private fun <T> List<T>.sparse(size: Int): List<T> {
    val skipSize = this.size / size
    return chunked(skipSize).map { it.first() }
}