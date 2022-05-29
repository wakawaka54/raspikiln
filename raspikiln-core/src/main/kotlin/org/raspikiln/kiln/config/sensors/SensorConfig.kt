package org.raspikiln.kiln.config.sensors

import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class SensorConfig(
    val name: String,
    val type: String,
    val sampling: SensorSamplingConfig?,
    val spi: SensorProtocolConfig.Spi?,
    val provides: List<SensorMeasurementConfig>,
) {
    fun requireSpi() = requireNotNull(spi) { "SPI was not defined but required for sensor $name" }
    inline fun <reified T : SensorMeasurementConfig> requireProvidesAs() = requireProvides(T::class)
    fun <T : SensorMeasurementConfig> requireProvides(type: KClass<T>) = provides.map {
        requireNotNull(type.safeCast(it)) { "Provides type of [$it] did not match expected [$type] on sensor [$name]" }
    }
}