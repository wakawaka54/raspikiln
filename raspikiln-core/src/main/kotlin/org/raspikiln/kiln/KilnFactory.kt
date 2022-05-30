package org.raspikiln.kiln

import org.raspikiln.kiln.bridge.KilnBridgeProviderRegistry
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.kiln.historical.KilnHistoricalTimeSeriesStore
import org.raspikiln.tsdb.TimesSeriesDB

class KilnFactory(
    private val timeSeriesDB: TimesSeriesDB,
    private val bridgeRegistry: KilnBridgeProviderRegistry,
    private val module: KilnModule
) {
    fun create(config: KilnConfig): Kiln {
        val kilnBridge = bridgeRegistry.find(config.provider).create(config)
        val initializer = module.initializationFactory()
        val stateService = module.stateService(kilnBridge)

        initializer.create(kilnBridge, stateService)

        return Kiln(kilnBridge, KilnHistoricalTimeSeriesStore(timeSeriesDB))
    }
}