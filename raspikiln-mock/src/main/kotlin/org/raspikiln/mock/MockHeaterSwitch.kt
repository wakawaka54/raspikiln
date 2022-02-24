package org.raspikiln.mock

import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.switches.SwitchType
import org.raspikiln.kiln.zones.KilnZoneName
import org.raspikiln.kiln.zones.KilnZoneNames

class MockHeaterSwitch(private val kilnState: MockKilnState) : Switch {
    private var state: SwitchState = SwitchState.Off

    init {
        toggle(SwitchState.Off)
    }

    override fun name(): String = "mock-heater-switch"

    override fun switchType(): SwitchType = SwitchType.HeaterSwitch

    override fun zones(): Set<KilnZoneName> = setOf(KilnZoneNames.Zone0)

    override fun switchState(): SwitchState = state

    override fun toggle(state: SwitchState) {
        this.state = state
        kilnState.toggleHeater(state)
    }
}