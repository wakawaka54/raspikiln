package org.raspikiln.server

import org.raspikiln.kiln.KilnDefinitionProviderRegistration
import org.raspikiln.kiln.config.KilnConfigDefinition

interface AppComponentFactory {
    companion object {
        fun koin(vararg definitionProviders: KilnDefinitionProviderRegistration): AppComponentFactory =
            KoinAppComponentFactory(definitionProviders.toList())
    }

    fun create(config: KilnConfigDefinition): AppComponent
}

class KoinAppComponentFactory(
    private val definitionProviders: List<KilnDefinitionProviderRegistration>
) : AppComponentFactory {
    override fun create(config: KilnConfigDefinition): AppComponent =
        AppComponent.create(serverModule(config = config, kilnDefinitionProviders = definitionProviders))
}