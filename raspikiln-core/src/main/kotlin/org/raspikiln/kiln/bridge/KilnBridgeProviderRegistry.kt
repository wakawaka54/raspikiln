package org.raspikiln.kiln.bridge

/**
 * Registry of kiln bridge providers..
 */
class KilnBridgeProviderRegistry {
    private val providers = mutableMapOf<String, KilnBridgeProvider>()

    fun register(provider: KilnBridgeProvider) = apply { providers.put(provider.name(), provider) }

    fun find(name: String) = requireNotNull(providers[name]) { "Missing kiln bridge provider [$name]" }
}