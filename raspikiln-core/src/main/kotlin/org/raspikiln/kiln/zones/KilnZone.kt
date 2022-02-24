package org.raspikiln.kiln.zones

import mu.KotlinLogging
import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.atomicValue
import org.raspikiln.kiln.programs.types.TemperatureSetpoint
import org.raspikiln.kiln.sensors.Sensor
import org.raspikiln.kiln.sensors.zoneTemperatureSensors
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.switches.heatingElementSwitch
import org.raspikiln.kiln.switches.maybeArmingSwitch

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
}