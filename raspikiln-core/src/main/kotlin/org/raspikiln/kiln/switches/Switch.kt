package org.raspikiln.kiln.switches

/**
 * Defines a switch kiln component.
 */
interface Switch {
    fun name(): String
    fun switchType(): SwitchType
    fun switchState(): SwitchState
    fun toggle(state: SwitchState)

    fun on() = toggle(SwitchState.On)
    fun off() = toggle(SwitchState.Off)

    fun measurements(): List<SwitchMeasurement>
}

enum class SwitchType {
    ArmingSwitch,
    HeaterSwitch
}

sealed class SwitchState(private val name: String, private val numeric: Int) {
    object On : SwitchState(name = "on", numeric = 1)
    object Off : SwitchState(name = "off", numeric = 0)

    fun name(): String = name
    fun numeric(): Int = numeric
}

fun Collection<SwitchState>.anyOn() = firstOrNull { it == SwitchState.On }

fun List<Switch>.maybeArmingSwitch() = filter { it.switchType() == SwitchType.ArmingSwitch }.aggregateOrNull()
fun List<Switch>.heatingElementSwitch() = filter { it.switchType() == SwitchType.HeaterSwitch }.aggregate()