package org.raspikiln.kiln.module

import org.raspikiln.core.Mapper
import org.raspikiln.core.time.SystemClock
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.bridge.KilnBridgeProviderRegistry
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.kiln.controller.KilnControllerRegistry
import org.raspikiln.kiln.controller.TemperatureController
import org.raspikiln.kiln.controller.TemperatureControllerFactory
import org.raspikiln.kiln.initialization.KilnInitializationFactory
import org.raspikiln.kiln.metrics.KilnMetricsRegistry
import org.raspikiln.kiln.metrics.KilnMetricsReporter
import org.raspikiln.kiln.programs.KilnProgramManager
import org.raspikiln.kiln.programs.ProgramFactory
import org.raspikiln.kiln.programs.ProgramStore
import org.raspikiln.kiln.state.KilnState
import org.raspikiln.kiln.state.KilnStateService

class KilnModule(
    private val systemClock: SystemClock,
    private val metrics: KilnMetricsRegistry,
    private val metricReporters: List<KilnMetricsReporter>,
    private val bridgeProviderRegistry: KilnBridgeProviderRegistry
) {
    fun systemClock() = systemClock
    fun initializationFactory() = KilnInitializationFactory(metrics)
    fun stateService(bridge: KilnBridge, state: KilnState) = KilnStateService(metrics, bridge, state)
    fun metricReporters(): List<KilnMetricsReporter> = metricReporters
    fun bridgeProviderRegistry() = bridgeProviderRegistry
    fun temperatureControllerFactory(bridge: KilnBridge, state: KilnState) = TemperatureControllerFactory(bridge, state)
    fun controllerRegistry(controllers: List<TemperatureController>) = KilnControllerRegistry(controllers)

    fun programManager(config: KilnConfig, controllers: KilnControllerRegistry, state: KilnState) =
        KilnProgramManager(store = programStore(config), factory = programFactory(controllers, state))

    private fun programStore(config: KilnConfig) = ProgramStore(config = config.programs)
    private fun programFactory(controllers: KilnControllerRegistry, state: KilnState) = ProgramFactory(
        controllers, state, Mapper.jsonMapper()
    )
}