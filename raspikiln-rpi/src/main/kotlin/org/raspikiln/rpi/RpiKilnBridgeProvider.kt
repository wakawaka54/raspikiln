package org.raspikiln.rpi

import mu.KotlinLogging
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.bridge.KilnBridgeProvider
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.rpi.core.PiContext

/**
 * Creates a RPI kiln.
 */
class RpiKilnBridgeProvider(
    private val rpiInitializer: RpiInitializer = RpiInitializer.native()
) : KilnBridgeProvider {
    override fun name(): String = "rpi"

    override fun create(config: KilnConfig): KilnBridge =
        with (initPi()) {
            RpiKilnBridge(
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
    private fun RpiKilnBridge.bindShutdown() = apply {
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