package org.raspikiln.kiln.controller

import org.raspikiln.core.services.scheduledService
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.controller.algorithm.ProportionalIntegralDerivative
import org.raspikiln.kiln.controller.algorithm.TupleTerms
import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.kiln.programs.types.TemperatureSetpoint
import org.raspikiln.kiln.state.KilnState
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.tsdb.utils.duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toKotlinDuration

/**
 * PID Controller.
 */
class PidController(
    override val name: String,
    override val armingSwitch: Switch?,
    override val heaterSwitch: Switch,
    private val state: KilnState,
    private val options: Options
) : TemperatureController {
    private val enabled = AtomicBoolean(false)
    private val setpoint = AtomicReference<TemperatureSetpoint>()
    private val previous = AtomicReference<Instant>()
    private val service = scheduledService(name = "$name-PID", delay = 0.seconds, period = options.dutyCycle) { loop() }

    private val pid = ProportionalIntegralDerivative(
        gainTerms = options.gainTerms,
        outputBounds = 0.0..50.0,
        outputScale = 50.0
    )

    override fun initialize(builder: KilnInitializationBuilder) {
        builder.registerService(service)
    }

    override fun error(): Double {
        val setpoint = setpoint.get()?.maybeTemperature()?.value ?: return 0.0
        val actual = state.requiredTemperature(options.controlTemperatureMetric).value
        return actual - setpoint
    }

    override fun set(setpoint: TemperatureSetpoint) {
        if (!enabled.getAndSet(true)) {
            reset()
            armingSwitch?.on()
            previous.set(state.timeNow())
        }

        this.setpoint.set(setpoint)
    }

    override fun unset() {
        reset()
        enabled.set(false)
    }

    private fun loop() {
        val setpoint = this.setpoint.get()?.maybeTemperature() ?: return

        // Update the state with the target temperature.
        state.update(options.targetTemperatureMetric, setpoint)

        // Get the actual temperature we are controlling.
        val now = state.timeNow()
        val actual = state.requiredTemperature(options.controlTemperatureMetric)
        val duration = (previous.get()..now).duration()

        val computation = pid.compute(
            duration = duration,
            actual = actual.celsius().value,
            target = setpoint.celsius().value
        )

        val heaterOn = dutyCycleMilliseconds() * computation.output
        val heaterOff = dutyCycleMilliseconds() * (1 - computation.output)

        sleep(SwitchState.On, heaterOn.milliseconds)
        sleep(SwitchState.Off, heaterOff.milliseconds)
    }

    private fun reset() {
        pid.reset()
        heaterSwitch.off()
        armingSwitch?.off()
    }

    private fun dutyCycleMilliseconds() = options.dutyCycle.inWholeMilliseconds.toDouble()

    private fun sleep(switchState: SwitchState, duration: Duration) {
        val nonZeroDuration = duration.takeIf { it.isPositive() } ?: return

        heaterSwitch.toggle(switchState)

        Thread.sleep(
            state.durationTime(nonZeroDuration).inWholeMilliseconds
        )
    }

    data class Options(
        val dutyCycle: Duration,
        val gainTerms: TupleTerms,
        val controlTemperatureMetric: String,
        val targetTemperatureMetric: String,
    )
}