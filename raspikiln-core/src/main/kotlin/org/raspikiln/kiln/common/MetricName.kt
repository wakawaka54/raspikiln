package org.raspikiln.kiln.common

import org.raspikiln.tsdb.MetricIdentifier

object MetricName {
    private const val TemperatureName = "temperature"
    private const val TemperatureTargetName = "temperatureTarget"

    val Temperature = MetricIdentifier(name = TemperatureName)
}