package org.raspikiln.kiln

import org.raspikiln.kiln.bridge.KilnBridgeProviderRegistry
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.kiln.historical.KilnHistoricalTimeSeriesStore
import org.raspikiln.kiln.initialization.KilnInitializer
import org.raspikiln.tsdb.TimesSeriesDB

class KilnFactory(
    private val timeSeriesDB: TimesSeriesDB,
    private val bridgeRegistry: KilnBridgeProviderRegistry,
    private val initializer: KilnInitializer
) {
    fun create(config: KilnConfig): Kiln {
        val kilnBridge = bridgeRegistry.find(config.provider).create(config)
        val initialization = initializer.initialize(kilnBridge).start()

        return Kiln(kilnBridge, KilnHistoricalTimeSeriesStore(timeSeriesDB))
    }
}