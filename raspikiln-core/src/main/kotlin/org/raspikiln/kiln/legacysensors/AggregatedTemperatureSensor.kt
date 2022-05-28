package org.raspikiln.kiln.legacysensors

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.common.requiredAll
import org.raspikiln.kiln.zones.KilnZoneName

/**
 * Aggregate multiple temperature sensors.
 */
fun Iterable<TemperatureSensor>.aggregate(): TemperatureSensor = AggregatedTemperatureSensor(this.toList())

/**
 * Temperature sensor which is made up of multiple measurement points.
 */
private class AggregatedTemperatureSensor(private val sensors: List<TemperatureSensor>) : TemperatureSensor {

    private val name = "aggregated-" + sensors.map { it.name() }.reduce { acc, next -> acc.commonPrefixWith(next) }
    private val location = sensors.first().location()

    init {
        require(sensors.all { it.location() == sensors.first().location() }) { "Aggregated sensors must all be in the same location." }
    }

    override fun name(): String = name

    override fun zone(): KilnZoneName = sensors.requiredAll { it.zone() }

    override fun location(): KilnLocation = location

    override fun read(): TemperatureMeasurement {
        val measurement = sensors.map { it.read().temperature.celsius().value }.average()
        return TemperatureMeasurement(
            temperature = Temperature.Celsius(measurement)
        )
    }
}