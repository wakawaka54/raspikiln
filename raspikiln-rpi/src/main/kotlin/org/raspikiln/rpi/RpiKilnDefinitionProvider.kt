package org.raspikiln.rpi

import mu.KotlinLogging
import org.raspikiln.kiln.KilnDefinition
import org.raspikiln.kiln.KilnDefinitionProvider
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.rpi.core.PiContext

private val logger = KotlinLogging.logger {  }

/**
 * Creates a RPI kiln.
 */
class RpiKilnDefinitionProvider(
    private val rpiInitializer: RpiInitializer = RpiInitializer.native()
) : KilnDefinitionProvider {
    override fun create(config: KilnConfig): KilnDefinition =
        with (initPi()) {
            RpiKilnDefinition(
                pi4j = this,
                sensors = createSensors(config),
                switches = createSwitches(config)
            )
            .bindShutdown()
        }

    /**
     * Initialize pi4j.
     */
    private fun initPi(): PiContext = rpiInitializer.init()

    /**
     * Bind a RPI kiln shutdown hook.
     */
    private fun RpiKilnDefinition.bindShutdown() = apply {
        Runtime.getRuntime().addShutdownHook(Thread { shutdown() })
    }

    private fun PiContext.createSensors(kilnConfig: KilnConfig) =
        with (RpiSensorProvider(this)) {
            kilnConfig.sensors.map { create(it) }
        }

    private fun PiContext.createSwitches(kilnConfig: KilnConfig) =
        with (RpiSwitchProvider(this)) {
            kilnConfig.switches.map { create(it) }
        }
}