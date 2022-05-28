package org.raspikiln.kiln.zones

import mu.KotlinLogging
import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.atomicValue
import org.raspikiln.kiln.programs.types.TemperatureSetpoint
import org.raspikiln.kiln.legacysensors.Sensor
import org.raspikiln.kiln.legacysensors.zoneTemperatureSensors
import org.raspikiln.kiln.switches.*

typealias KilnZoneName = String

private val logger = KotlinLogging.logger { }

/**
 * A zone within the kiln which can be controlled.
 */
class KilnZone(
    val name: KilnZoneName,
    val sensors: List<Sensor>,
    val switches: List<Switch>
) {
    private val armingSwitch: Switch? = switches.maybeArmingSwitch()
    private val temperatureSensor = sensors.zoneTemperatureSensors(zoneName = name)
    private val heatingElementSwitch: Switch = switches.heatingElementSwitch()

    private var temperatureTarget: Temperature? by atomicValue()

    /**
     * Get the current state of the kiln zone.
     */
    fun state() = ZoneState(
        name = name,
        temperature = temperatureSensor.read().temperature,
        heaterSwitchState = heatingElementSwitch.switchState(),
        armingSwitchState = armingSwitch?.switchState() ?: heatingElementSwitch.switchState(),
        temperatureSetpoint = temperatureTarget
    )

    fun target(setpoint: TemperatureSetpoint) {
        temperatureTarget = setpoint.maybeTemperature()
    }

    fun target(): Temperature? = temperatureTarget

    /**
     * Arm the heating element.
     */
    fun armingState(switchState: SwitchState) {
        val armingState = armingSwitch ?: return
        if (switchState != armingState.switchState()) {
            logger.info { "Kiln Zone $name arming switch set to [${switchState.name()}]" }
            armingState.toggle(switchState)
        }
    }

    /**
     * Turn the heater on or off.
     */
    fun heaterState(switchState: SwitchState) {
        if (switchState != heatingElementSwitch.switchState()) {
            logger.info { "Kiln Zone $name heater switch set to [${switchState.name()}]" }
            heatingElementSwitch.toggle(switchState)
        }
    }

    fun temperatureSensors() = listOf(temperatureSensor)

    fun heaterState() = heatingElementSwitch.switchState()
}

fun List<KilnZone>.target() = mapNotNull { it.target() }.maxOfOrNull { it.celsius().value }
fun List<KilnZone>.heaterState() = map { it.heaterState() }.anyOn() ?: SwitchState.Off
fun List<KilnZone>.temperatureSensors() = flatMap { it.temperatureSensors() }.toSet().toList()