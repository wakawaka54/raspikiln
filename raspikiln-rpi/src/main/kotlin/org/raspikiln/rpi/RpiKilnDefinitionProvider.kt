package org.raspikiln.rpi

import mu.KotlinLogging
import org.raspikiln.kiln.BaseKilnDefinitionProvider
import org.raspikiln.kiln.KilnBridge
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.rpi.core.PiContext

private val logger = KotlinLogging.logger {  }

/**
 * Creates a RPI kiln.
 */
class RpiKilnDefinitionProvider(
    private val rpiInitializer: RpiInitializer = RpiInitializer.native()
) : BaseKilnDefinitionProvider() {
    override fun create(config: KilnConfig): KilnBridge =
        with (initPi()) {
            RpiKilnDefinition(
                pi4j = this,
                sensors = createSensors(config),
                switches = createSwitches(config),
                controllers = config.createControllers()
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