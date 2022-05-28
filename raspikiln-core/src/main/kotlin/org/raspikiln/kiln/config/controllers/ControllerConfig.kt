package org.raspikiln.kiln.config.controllers

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.raspikiln.kiln.zones.KilnZoneName

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface ControllerConfig {
    @JsonTypeName("on-off")
    data class OnOff(
        val dutyCycle: Int,
        val hysteresis: Double,
        val zones: List<KilnZoneName>
    ) : ControllerConfig

    @JsonTypeName("pid")
    data class PID(
        val dutyCycle: Int,
        val kp: Double,
        val ki: Double,
        val kd: Double,
        val zones: List<KilnZoneName>
    ) : ControllerConfig
}