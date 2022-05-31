package org.raspikiln.mock

import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.switches.SwitchMeasurement
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.switches.SwitchType

class MockHeaterSwitch(
    private val name: String,
    private val metric: String,
    private val kilnState: MockKilnState
) : Switch {
    private var state: SwitchState = SwitchState.Off

    init {
        toggle(SwitchState.Off)
    }

    override fun name(): String = name

    override fun switchType(): SwitchType = SwitchType.HeaterSwitch

    override fun switchState(): SwitchState = state

    override fun toggle(state: SwitchState) {
        this.state = state
        kilnState.toggleHeater(state)
    }

    override fun measurements(): List<SwitchMeasurement> = listOf(
        SwitchMeasurement(
            metric = metric,
            state = switchState()
        )
    )
}