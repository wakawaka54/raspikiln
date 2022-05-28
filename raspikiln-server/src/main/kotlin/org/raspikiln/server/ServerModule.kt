package org.raspikiln.server

import org.koin.dsl.module
import org.raspikiln.kiln.KilnDefinitionProviderRegistration
import org.raspikiln.kiln.KilnFactory
import org.raspikiln.kiln.config.KilnConfigDefinition

fun serverModule(
    config: KilnConfigDefinition,
    kilnDefinitionProviders: List<KilnDefinitionProviderRegistration>
) = module {

    single { config }

    single {
        KilnFactory(
                timeSeriesDB = get(),
                definitionProviders = getAll<KilnDefinitionProviderRegistration>().associate { it.name to it.provider }
            )
            .create(config.kiln)
    }

    kilnDefinitionProviders.forEach { definitionProvider -> single { definitionProvider } }

    single {
        KilnApplication(
            kilnConfigDefinition = get(),
            kiln = get()
        )
    }
}