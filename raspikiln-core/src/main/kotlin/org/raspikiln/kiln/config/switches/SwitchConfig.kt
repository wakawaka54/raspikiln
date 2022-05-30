package org.raspikiln.kiln.config.switches

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.config.sensors.SensorProtocolConfig

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface SwitchConfig {

    @JsonTypeName("arming-switch")
    data class ArmingSwitchConfig(
        val name: String,
        val digitalOutput: SensorProtocolConfig.DigitalOutput
    ) : SwitchConfig

    @JsonTypeName("heating-element-switch")
    data class HeatingElementSwitchConfig(
        val name: String,
        val digitalOutput: SensorProtocolConfig.DigitalOutput
    ) : SwitchConfig
}