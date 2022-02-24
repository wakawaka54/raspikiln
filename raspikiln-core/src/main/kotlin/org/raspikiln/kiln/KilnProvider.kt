package org.raspikiln.kiln

import org.raspikiln.core.raspikilnException
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.kiln.historical.KilnHistoricalTimeSeriesStore
import org.raspikiln.tsdb.TimesSeriesDB

class KilnProvider(
    private val timeSeriesDB: TimesSeriesDB,
    private val definitionProviders: Map<String, KilnDefinitionProvider>
) {
    fun create(config: KilnConfig) =
        Kiln(
            definition = config.createKilnDefinition(),
            historicalStore = KilnHistoricalTimeSeriesStore(timeSeriesDB)
        )

    private fun KilnConfig.createKilnDefinition() =
        definitionProviders[provider]?.create(this) ?: raspikilnException(message = "Missing kiln provider $provider")
}