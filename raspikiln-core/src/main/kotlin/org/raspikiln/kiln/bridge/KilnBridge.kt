package org.raspikiln.kiln.bridge

import org.raspikiln.kiln.controller.TemperatureController
import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.kiln.sensors.Sensor
import org.raspikiln.kiln.switches.Switch

/**
 * Defines the kiln hardware.
 */
interface KilnBridge {
    /**
     * Initialize the kiln bridge.
     */
    fun initialize(builder: KilnInitializationBuilder)

    /**
     * Get sensors from the kiln bridge.
     */
    fun sensors(): List<Sensor>

    /**
     * Get switches from the kiln bridge.
     */
    fun switches(): List<Switch>
    fun controllers(): List<TemperatureController>

    fun shutdown()
}
