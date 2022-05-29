package org.raspikiln.server

import org.koin.dsl.module
import org.raspikiln.kiln.KilnFactory
import org.raspikiln.kiln.bridge.KilnBridgeProviderRegistry
import org.raspikiln.kiln.config.Config
import org.raspikiln.kiln.initialization.KilnInitializer

fun serverModule(
    config: Config,
    bridgeProviderRegistry: KilnBridgeProviderRegistry
) = module {

    single { config }

    single {
        KilnFactory(
                timeSeriesDB = get(),
                bridgeRegistry = bridgeProviderRegistry,
                initializer = KilnInitializer()
            )
            .create(config.kiln)
    }

    single {
        KilnApplication(
            config = get(),
            kiln = get()
        )
    }
}