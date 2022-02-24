package org.raspikiln.kiln.config

/**
 * High level kiln config file.
 */
data class KilnConfigDefinition(
    val kiln: KilnConfig,
    val http: HttpConfig = HttpConfig(enabled = false)
)

/**
 * Kiln configuration.
 */
data class KilnConfig(

    /**
     * The name of the provider that creates a kiln.
     */
    val provider: String,

    /**
     * Sensors configured in the Kiln.
     */
    val sensors: List<SensorConfig> = emptyList(),

    /**
     * Switches configured in the kiln.
     */
    val switches: List<SwitchConfig> = emptyList()
)