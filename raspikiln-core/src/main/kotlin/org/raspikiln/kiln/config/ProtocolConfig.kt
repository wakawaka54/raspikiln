package org.raspikiln.kiln.config

sealed interface ProtocolConfig {
    data class Spi(
        val address: Int?,
        val baud: Int?
    ) : ProtocolConfig

    data class DigitalOutput(
        val pin: Int?
    )
}