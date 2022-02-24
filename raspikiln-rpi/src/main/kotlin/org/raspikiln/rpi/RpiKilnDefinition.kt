package org.raspikiln.rpi

import mu.KotlinLogging
import org.raspikiln.kiln.KilnDefinition
import org.raspikiln.kiln.sensors.Sensor
import org.raspikiln.kiln.sensors.zones
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.switches.zones
import org.raspikiln.kiln.zones.KilnZoneName
import org.raspikiln.rpi.core.PiContext

private val logger = KotlinLogging.logger {  }

/**
 * Implements a Kiln which is controlled by a Raspberry Pi.
 */
class RpiKilnDefinition(
    private val pi4j: PiContext,
    private val sensors: List<Sensor>,
    private val switches: List<Switch>
) : KilnDefinition {
    override fun zones(): Set<KilnZoneName> = sensors.zones() + switches.zones()

    override fun sensors(): List<Sensor> = sensors

    override fun switches(): List<Switch> = switches

    override fun shutdown() {
        logger.info { "Shutting down pi4j." }
        try {
            pi4j.shutdown()
        } catch (ex: Exception) {
            logger.error(ex) { "pi4j shutdown failed." }
        }
    }
}