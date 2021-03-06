package org.raspikiln.kiln.switches

import org.raspikiln.kiln.zones.KilnZoneName

/**
 * Defines a switch kiln component.
 */
interface Switch {
    fun name(): String
    fun switchType(): SwitchType
    fun switchState(): SwitchState
    fun toggle(state: SwitchState)
    fun zones(): Set<KilnZoneName>

    fun on() = toggle(SwitchState.On)
    fun off() = toggle(SwitchState.Off)
}

enum class SwitchType {
    ArmingSwitch,
    HeaterSwitch
}

sealed class SwitchState(private val name: String) {
    object On : SwitchState(name = "on")
    object Off : SwitchState(name = "off")

    fun name(): String = name
}

fun Collection<SwitchState>.anyOn() = firstOrNull { it == SwitchState.On }

fun List<Switch>.filterZone(zone: KilnZoneName) = filter { it.zones().contains(zone) }
fun List<Switch>.zones() = flatMap { it.zones() }.toSet()
fun List<Switch>.maybeArmingSwitch() = filter { it.switchType() == SwitchType.ArmingSwitch }.aggregateOrNull()
fun List<Switch>.heatingElementSwitch() = filter { it.switchType() == SwitchType.HeaterSwitch }.aggregate()