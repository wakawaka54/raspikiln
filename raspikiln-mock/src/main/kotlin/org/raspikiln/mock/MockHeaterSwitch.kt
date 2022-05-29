package org.raspikiln.mock

import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.switches.SwitchType

class MockHeaterSwitch(
    private val kilnState: MockKilnState,
    private val locations: Set<KilnLocation>
) : Switch {
    private var state: SwitchState = SwitchState.Off

    init {
        toggle(SwitchState.Off)
    }

    override fun name(): String = "mock-heater-switch"

    override fun switchType(): SwitchType = SwitchType.HeaterSwitch

    override fun locations(): Set<KilnLocation> = locations

    override fun switchState(): SwitchState = state

    override fun toggle(state: SwitchState) {
        this.state = state
        kilnState.toggleHeater(state)
    }
}