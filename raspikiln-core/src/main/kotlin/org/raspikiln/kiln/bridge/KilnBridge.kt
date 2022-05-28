package org.raspikiln.kiln.bridge

import org.raspikiln.kiln.controller.TemperatureController
import org.raspikiln.kiln.legacysensors.Sensor
import org.raspikiln.kiln.legacysensors.filterZone
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.switches.filterZone
import org.raspikiln.kiln.zones.KilnZone
import org.raspikiln.kiln.zones.KilnZoneName

/**
 * Defines the kiln hardware.
 */
interface KilnBridge {
    fun zones(): Set<KilnZoneName>
    fun sensors(): List<Sensor>
    fun switches(): List<Switch>
    fun controllers(): List<TemperatureController>

    fun shutdown()
}

/**
 * Resolve kiln zones.
 */
fun KilnBridge.kilnZones() = zones().map { zone ->
    KilnZone(
        name = zone,
        sensors = sensors().filterZone(zone),
        switches = switches().filterZone(zone)
    )
}
