package org.raspikiln.server

import org.raspikiln.kiln.bridge.KilnBridgeProviderRegistry
import org.raspikiln.kiln.config.Config

interface AppComponentFactory {
    companion object {
        fun create(bridgeRegistry: KilnBridgeProviderRegistry): AppComponentFactory =
            KoinAppComponentFactory(bridgeRegistry)
    }

    fun create(config: Config): AppComponent
}

class KoinAppComponentFactory(
    private val bridgeRegistry: KilnBridgeProviderRegistry
) : AppComponentFactory {
    override fun create(config: Config): AppComponent =
        AppComponent.create(serverModule(config = config, bridgeProviderRegistry = bridgeRegistry))
}