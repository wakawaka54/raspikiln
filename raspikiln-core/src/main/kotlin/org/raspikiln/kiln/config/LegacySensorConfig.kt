package org.raspikiln.kiln.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.zones.KilnZoneName

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface LegacySensorConfig {
    @JsonTypeName("temperature")
    data class TemperatureSensorConfig(
        val name: String,
        val location: KilnLocation = KilnLocation.Oven,
        val zone: KilnZoneName,
        val sensor: LegacySpecificSensor
    ) : LegacySensorConfig
}