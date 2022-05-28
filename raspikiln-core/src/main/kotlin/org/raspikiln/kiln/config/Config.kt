package org.raspikiln.kiln.config

data class Config(
    /**
     * Kiln hardware configuration.
     */
    val kiln: KilnConfig,

    /**
     * Http server configuration.
     */
    val http: HttpConfig
)