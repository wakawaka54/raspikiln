package org.raspikiln.kiln.config.sensors

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.zones.KilnZoneName

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
interface SensorMeasurementConfig {

    @JsonTypeName("temperature")
    data class Temperature(
        val name: String,
        val location: KilnLocation = KilnLocation.Oven,
        val zone: KilnZoneName,
    )
}