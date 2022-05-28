package org.raspikiln.kiln.config.sensors

data class SensorConfig(
    val name: String,
    val type: String,
    val sampling: SensorSamplingConfig?,
    val spi: SensorProtocolConfig.Spi?,
    val provides: List<SensorMeasurementConfig>,
)