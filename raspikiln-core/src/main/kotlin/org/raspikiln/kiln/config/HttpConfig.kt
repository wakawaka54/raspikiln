package org.raspikiln.kiln.config

data class HttpConfig(
    val enabled: Boolean = true,
    val port: Int = 8080
)