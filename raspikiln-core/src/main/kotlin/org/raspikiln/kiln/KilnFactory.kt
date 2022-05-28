package org.raspikiln.kiln

import org.raspikiln.kiln.bridge.KilnBridgeProviderRegistry
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.kiln.historical.KilnHistoricalTimeSeriesStore
import org.raspikiln.tsdb.TimesSeriesDB

class KilnFactory(
    private val timeSeriesDB: TimesSeriesDB,
    private val bridgeRegistry: KilnBridgeProviderRegistry
) {
    fun create(config: KilnConfig) =
        Kiln(
            bridge = bridgeRegistry.find(config.provider).create(config),
            historicalStore = KilnHistoricalTimeSeriesStore(timeSeriesDB)
        )
}