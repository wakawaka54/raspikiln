package org.raspikiln.kiln.config.sensors

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class SensorConfig(
    val name: String,
    val type: String,
    val sampling: SensorSamplingConfig?,
    val chipset: SensorChipsetConfig,
    val provides: List<SensorMeasurementConfig>
) {
    inline fun <reified T : SensorMeasurementConfig> requireProvidesAs() = requireProvides(T::class)
    fun <T : SensorMeasurementConfig> requireProvides(type: KClass<T>) = provides.map {
        requireNotNull(type.safeCast(it)) { "Provides type of [$it] did not match expected [$type] on sensor [$name]" }
    }
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface SensorChipsetConfig {
    @JsonTypeName("MAX31855")
    data class MAX31855(val spi: SensorProtocolConfig.Spi) : SensorChipsetConfig

    @JsonTypeName("mock")
    class Mock : SensorChipsetConfig
}