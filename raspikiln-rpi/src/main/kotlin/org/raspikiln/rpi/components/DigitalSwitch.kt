package org.raspikiln.rpi.components

import com.pi4j.io.gpio.digital.DigitalOutput
import com.pi4j.io.gpio.digital.DigitalState
import mu.KotlinLogging
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.switches.*
import org.raspikiln.rpi.components.DigitalSwitchState.digitalState
import org.raspikiln.rpi.components.DigitalSwitchState.switchState

private val logger = KotlinLogging.logger { }

/**
 * Simple digital out switch.
 */
class DigitalSwitch(
    private val name: String,
    private val locations: Set<KilnLocation>,
    private val switchType: SwitchType,
    private val digitalOutput: DigitalOutput
) : Switch {

    init {
        digitalOutput.low()
        digitalOutput.config().shutdownState(DigitalState.LOW)
    }

    fun pin() = digitalOutput.address()

    override fun name(): String = name

    override fun switchType(): SwitchType = switchType

    override fun locations(): Set<KilnLocation> = locations

    override fun switchState(): SwitchState = digitalOutput.state().switchState()

    override fun toggle(state: SwitchState) {
        digitalOutput.state(state.digitalState())
    }
}


private object DigitalSwitchState {
    private val SwitchStateMap = mapOf(
        DigitalState.HIGH to SwitchState.On,
        DigitalState.LOW to SwitchState.Off
    )
    private val Reversed = SwitchStateMap.entries.associateBy({ it.value }) { it.key }

    fun DigitalState.switchState(): SwitchState = SwitchStateMap[this] ?: error("Could not map digital state $this")

    fun SwitchState.digitalState(): DigitalState = Reversed[this] ?: error("Could not map switch state [$this]")
}