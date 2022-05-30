package org.raspikiln.kiln.config.controllers

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.raspikiln.kiln.common.KilnLocation

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface ControllerConfig {
    val armingSwitch: String?
    val heaterSwitch: String
    val targetMetric: String

    @JsonTypeName("on-off")
    data class OnOff(
        val dutyCycle: Int,
        val hysteresis: Double,
        val locations: List<KilnLocation>,
        override val armingSwitch: String?,
        override val heaterSwitch: String,
        override val targetMetric: String
    ) : ControllerConfig

    @JsonTypeName("pid")
    data class PID(
        val dutyCycle: Int,
        val kp: Double,
        val ki: Double,
        val kd: Double,
        override val armingSwitch: String?,
        override val heaterSwitch: String,
        override val targetMetric: String
    ) : ControllerConfig
}