package org.raspikiln.kiln.bridge

import org.raspikiln.kiln.controller.TemperatureController
import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.kiln.initialization.KilnInitializer
import org.raspikiln.kiln.sensors.Sensor
import org.raspikiln.kiln.switches.Switch

/**
 * Defines the kiln hardware.
 */
interface KilnBridge : KilnInitializer {

    /**
     * Get sensors from the kiln bridge.
     */
    fun sensors(): List<Sensor>

    /**
     * Get switches from the kiln bridge.
     */
    fun switches(): List<Switch>

    fun findSensor(name: String) = sensors().find { it.name() == name }
    fun requireSensor(name: String) = requireNotNull(findSensor(name)) { "Could not find required sensor [$name]" }

    fun findSwitch(name: String) = switches().find { it.name() == name }
    fun requireSwitch(name: String) = requireNotNull(findSwitch(name)) { "Could not find required switch [$name]" }

    fun shutdown()
}
