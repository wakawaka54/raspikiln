package org.raspikiln.kiln.config.sensors

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.raspikiln.kiln.common.KilnLocation

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface SensorMeasurementConfig {

    @JsonTypeName("temperature")
    data class Temperature(

        /**
         * Friendly name for the sensor.
         */
        val name: String,

        /**
         * Address identifier which is specific to the temperature sensor.
         */
        val address: String,

        /**
         * Metric name of the temperature sensor.
         */
        val metric: String
    ) : SensorMeasurementConfig
}