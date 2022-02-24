package org.raspikiln.server

import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.raspikiln.controller.ControllerModule
import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.KilnDefinitionProviderRegistration
import org.raspikiln.kiln.KilnProvider
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.kiln.config.KilnConfigDefinition
import org.raspikiln.kiln.historical.KilnHistoricalTimeSeriesStore
import org.raspikiln.rpi.RpiKilnDefinitionProvider
import org.raspikiln.server.config.ServerKilnDefinitionProvider
import org.raspikiln.server.config.serverKilnProvider
import org.raspikiln.server.core.CoreModule
import org.raspikiln.server.core.startKoinApplication

fun koinApp(vararg modules: Module): (KilnConfigDefinition) -> Koin = { config ->
    startKoinApplication {
        modules(serverModule(config) + modules)
    }
}

fun serverModule(config: KilnConfigDefinition) = module {
    single { config }

    single(createdAtStart = true) {
        KilnProvider(
                timeSeriesDB = get(),
                definitionProviders = getAll<KilnDefinitionProviderRegistration>().associate { it.name to it.provider }
            )
            .create(config.kiln)
            .start()
    }

    single {
        KilnDefinitionProviderRegistration(
            name = "rpi",
            provider = RpiKilnDefinitionProvider()
        )
    }

    single { KilnApplication() }

} + CoreModule + ControllerModule