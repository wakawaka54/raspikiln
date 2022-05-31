package org.raspikiln.kiln

import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.kiln.controller.KilnControllerRegistry
import org.raspikiln.kiln.controller.TemperatureController
import org.raspikiln.kiln.initialization.initialize
import org.raspikiln.kiln.module.KilnModule
import org.raspikiln.kiln.module.KilnModuleSpec
import org.raspikiln.kiln.module.kilnModule
import org.raspikiln.kiln.state.KilnState
import org.raspikiln.tsdb.TimesSeriesDB

fun kilnFactory(moduleSpec: KilnModuleSpec.() -> Unit) =
    KilnFactory(kilnModule(init = moduleSpec))

class KilnFactory(
    private val module: KilnModule
) {
    fun create(config: KilnConfig): Kiln {

        val state = KilnState(module.systemClock())
        val kilnBridge = module.createKilnBridge(config)
        val controllers = module.createControllers(config, kilnBridge, state)
        val programManager = module.programManager(config, controllers, state)

        module.initializationFactory()
            .initialize {
                initialize(
                    kilnBridge,
                    module.stateService(kilnBridge, state),
                    *module.metricReporters().toTypedArray(),
                    programManager,
                    controllers
                )
            }
            .start()

        return Kiln(kilnBridge, state, programManager)
    }

    private fun KilnModule.createKilnBridge(config: KilnConfig) =
        bridgeProviderRegistry().find(config.provider).create(config)

    private fun KilnModule.createControllers(config: KilnConfig, bridge: KilnBridge, state: KilnState): KilnControllerRegistry {
        val factory = temperatureControllerFactory(bridge, state)
        return controllerRegistry(controllers = config.controllers.map { factory.create(it) })
    }
}