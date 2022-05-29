package org.raspikiln.kiln.config

import org.raspikiln.kiln.config.http.HttpConfig

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