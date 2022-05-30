package org.raspikiln.server

import org.koin.dsl.module
import org.raspikiln.kiln.KilnFactory
import org.raspikiln.kiln.bridge.KilnBridgeProvider
import org.raspikiln.kiln.bridge.KilnBridgeProviderRegistry
import org.raspikiln.kiln.initialization.KilnInitializer
import org.raspikiln.rpi.RpiKilnBridgeProvider
import org.raspikiln.server.core.CoreModule

val AppModule = module {

    single { KilnBridgeProviderRegistry().registerAll(providers = getAll()) }

    single { KilnFactory(timeSeriesDB = get(), bridgeRegistry = get(), initializer = KilnInitializer()) }

    single { KilnApplication(kilnFactory = get()) }
} + CoreModule

val RpiModule = module {
    single<KilnBridgeProvider> { RpiKilnBridgeProvider() }
}