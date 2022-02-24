package org.raspikiln.kiln.common

import org.raspikiln.kiln.zones.KilnZoneName
import org.raspikiln.tsdb.MetricIdentifier

object MetricName {
    private const val TemperatureName = "temperature"
    private const val TemperatureTargetName = "temperatureTarget"

    val Temperature = MetricIdentifier(name = TemperatureName)

    fun temperature(zone: KilnZoneName) = MetricIdentifier(name = "${TemperatureName}_$zone")
    fun temperatureTarget(zone: KilnZoneName) = MetricIdentifier(name = "${TemperatureTargetName}_$zone")
}