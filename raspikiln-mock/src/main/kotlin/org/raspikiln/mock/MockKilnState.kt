package org.raspikiln.mock

import org.raspikiln.kiln.switches.SwitchState

class MockKilnState(private val options: Options) {
    private var heaterSwitchState: SwitchState = SwitchState.Off
    private val temperatureFormula = options.temperatureFormula

    fun heaterSwitchState() = heaterSwitchState

    fun temperature() = temperatureFormula.currentTemperature()

    fun toggleHeater(state: SwitchState) {
        this.heaterSwitchState = state
    }

    fun update() {
        temperatureFormula.computeNext(
            MockTemperatureFormula.ComputationState(
            elementState = heaterSwitchState
        ))
    }

    data class Options(
        val temperatureFormula: MockTemperatureFormula
    )
}