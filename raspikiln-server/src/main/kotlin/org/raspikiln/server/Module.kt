package org.raspikiln.server

import org.koin.dsl.module
import org.raspikiln.kiln.bridge.KilnBridgeProvider
import org.raspikiln.kiln.kilnFactory
import org.raspikiln.kiln.metrics.reporters.historicalTimeSeriesMetricsReporter
import org.raspikiln.rpi.RpiKilnBridgeProvider
import org.raspikiln.server.config.ConfigModule
import org.raspikiln.server.core.CoreModule

val AppModule = module {

    single {
        kilnFactory {
            bridgeProviders(bridgeProviders = getAll())

            historicalTimeSeriesMetricsReporter(timesSeriesDB = get())
        }
    }

    single { KilnApplication(kilnFactory = get()) }
} + CoreModule + ConfigModule

val RpiModule = module {
    single<KilnBridgeProvider> { RpiKilnBridgeProvider() }
}