package org.raspikiln.kiln.config

import org.raspikiln.kiln.config.controllers.ControllerConfig
import org.raspikiln.kiln.config.programs.ProgramConfig
import org.raspikiln.kiln.config.sensors.SensorConfig
import org.raspikiln.kiln.config.switches.SwitchConfig

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
    val switches: List<SwitchConfig> = emptyList(),

    /**
     * Controllers configured in the kiln.
     */
    val controllers: List<ControllerConfig> = emptyList(),

    /**
     * Programs configured for the kiln.
     */
    val programs: List<ProgramConfig> = emptyList()
)