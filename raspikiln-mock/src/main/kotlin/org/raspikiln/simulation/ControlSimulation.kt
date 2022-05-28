package org.raspikiln.simulation

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.controller.ControllerInput
import org.raspikiln.kiln.controller.PidController
import org.raspikiln.kiln.controller.algorithm.TupleTerms
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.zones.KilnZoneNames
import org.raspikiln.mock.MockTemperatureFormula
import org.raspikiln.mock.MockTemperatureFormula.Parameters
import org.raspikiln.mock.RoomTemperature
import org.raspikiln.tsdb.utils.plus
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Creates a control simulation.
 */
class ControlSimulation(options: Options) {
    val startTime = options.startTime
    val gainTerms = options.gainTerms

    private val dutyCycle = options.dutyCycle
    private val temperatureFormula = MockTemperatureFormula(Parameters(
        startTime = options.startTime,
        kilnTemperature = options.kilnTemperature.value
    ))
    private val controller = PidController(
        zones = setOf(KilnZoneNames.Zone0),
        options = PidController.Options(
            dutyCycle = dutyCycle,
            gainTerms = options.gainTerms
        )
    )

    private var currentTime = options.startTime
    private var switchState: SwitchState = SwitchState.Off
    private var setpoint: Temperature? = options.setpoint

    fun setpoint(setpoint: Temperature) = apply {
        this.controller.reset()
        this.setpoint = setpoint
    }

    fun state(): SimulationState =
        SimulationState(
            timestamp = currentTime,
            temperature = temperatureFormula.currentTemperature(),
            switchState = switchState,
            setpoint = setpoint
        )

    fun sequence(): Sequence<SimulationStep> = generateSequence { nextStep() }

    fun sequence(simulationLength: Duration): Sequence<SimulationStep> {
        val startTime = currentTime
        return sequence().takeWhile { it.state().timestamp < startTime + simulationLength }
    }

    fun nextStep(): SimulationStep {
        val state = state();
        val setpoint = state.setpoint ?: return nextStepHeaterOff()
        val controllerOutput = controller.evaluate(
            ControllerInput(
                setpoint = setpoint,
                temperature = state.temperature,
                heaterState = state.switchState,
                dutyCycle = dutyCycle
            )
        )

        val states = listOf(
            controllerOutput.heaterOn to SwitchState.On,
            controllerOutput.heaterOff to SwitchState.Off
        ).mapNotNull { (duration, switchState) -> incrementTime(duration, switchState) }

        return SimulationStep(
            heaterOn = controllerOutput.heaterOn,
            heaterOff = controllerOutput.heaterOff,
            states = listOf(state) + states
        )
    }

    private fun nextStepHeaterOff(): SimulationStep =
        SimulationStep(
            heaterOn = 0.seconds,
            heaterOff = dutyCycle,
            states = listOf(
                state(),
                incrementTime(duration = dutyCycle, switchState = SwitchState.Off)!!
            )
        )

    private fun incrementTime(duration: Duration, switchState: SwitchState): SimulationState? {
        this.currentTime += duration.takeIf { it > 0.seconds } ?: return null
        this.switchState = switchState
        temperatureFormula.computeNext(
            MockTemperatureFormula.ComputationState(
                currentTime = currentTime,
                elementState = switchState
            )
        )

        return state()
    }

    data class Options(
        val startTime: Instant = Instant.now(),
        val dutyCycle: Duration = 2.seconds,
        val kilnTemperature: Temperature = RoomTemperature,
        val gainTerms: TupleTerms,
        val setpoint: Temperature? = null
    )
}

data class SimulationState(
    val timestamp: Instant,
    val temperature: Temperature,
    val switchState: SwitchState,
    val setpoint: Temperature?
)

data class SimulationStep(
    val heaterOn: Duration,
    val heaterOff: Duration,
    val states: List<SimulationState>
) {
    fun state() = states.last()
}