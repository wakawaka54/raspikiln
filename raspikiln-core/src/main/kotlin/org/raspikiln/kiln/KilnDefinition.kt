package org.raspikiln.kiln

import org.raspikiln.kiln.sensors.Sensor
import org.raspikiln.kiln.sensors.filterZone
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.switches.filterZone
import org.raspikiln.kiln.zones.KilnZone
import org.raspikiln.kiln.zones.KilnZoneName

interface KilnDefinition {
    fun zones(): Set<KilnZoneName>
    fun sensors(): List<Sensor>
    fun switches(): List<Switch>

    fun shutdown()
}

/**
 * Resolve kiln zones.
 */
fun KilnDefinition.kilnZones() = zones().map { zone ->
    KilnZone(
        name = zone,
        sensors = sensors().filterZone(zone),
        switches = switches().filterZone(zone)
    )
}
