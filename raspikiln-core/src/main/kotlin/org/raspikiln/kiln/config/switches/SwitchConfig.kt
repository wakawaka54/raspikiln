package org.raspikiln.kiln.config.switches

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.raspikiln.kiln.config.sensors.SensorProtocolConfig

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface SwitchConfig {

    @JsonTypeName("digital-switch")
    data class DigitalSwitch(
        val name: String,
        val metric: String,
        val digitalOutput: SensorProtocolConfig.DigitalOutput
    ) : SwitchConfig
}