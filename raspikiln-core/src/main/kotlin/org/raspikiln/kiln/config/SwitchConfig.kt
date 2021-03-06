package org.raspikiln.kiln.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.raspikiln.kiln.zones.KilnZoneName

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface SwitchConfig {

    @JsonTypeName("arming-switch")
    data class ArmingSwitchConfig(
        val name: String,
        val zones: List<KilnZoneName>,
        val digitalOutput: ProtocolConfig.DigitalOutput
    ) : SwitchConfig

    @JsonTypeName("heating-element-switch")
    data class HeatingElementSwitchConfig(
        val name: String,
        val zones: List<KilnZoneName>,
        val digitalOutput: ProtocolConfig.DigitalOutput
    ) : SwitchConfig
}