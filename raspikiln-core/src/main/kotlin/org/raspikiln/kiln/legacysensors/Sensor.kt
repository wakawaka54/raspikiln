package org.raspikiln.kiln.legacysensors

import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.zones.KilnZoneName

/**
 * Defines a kiln sensor.
 */
interface Sensor {
    fun name(): String
    fun zone(): KilnZoneName
}

/**
 * Filter by zone.
 */
fun List<Sensor>.filterZone(zone: KilnZoneName) = filter { it.zone() == zone }

/**
 * Zones from sensors.
 */
fun List<Sensor>.zones() = map { it.zone() }.toSet()

/**
 * Filters sensors by oven temperature sensors.
 */
fun List<Sensor>.zoneTemperatureSensors(zoneName: KilnZoneName) =
    filterIsInstance<TemperatureSensor>()
        .filter { it.location() == KilnLocation.Oven }
        .filter { it.zone() == zoneName }
        .aggregate()