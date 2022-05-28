package org.raspikiln.kiln.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.raspikiln.kiln.config.sensors.SensorProtocolConfig

/**
 * Specific sensor config, for configuring specific sensor types.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface LegacySpecificSensor

/**
 * MAX31855 thermocouple sensor.
 */
@JsonTypeName("MAX31855")
data class MAX31855Config(
    val spi: SensorProtocolConfig.Spi
) : LegacySpecificSensor