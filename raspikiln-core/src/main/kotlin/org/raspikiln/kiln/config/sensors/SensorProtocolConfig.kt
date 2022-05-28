package org.raspikiln.kiln.config.sensors

sealed interface SensorProtocolConfig {
    data class Spi(
        val address: Int?,
        val baud: Int?
    ) : SensorProtocolConfig

    data class DigitalOutput(
        val pin: Int?
    )
}