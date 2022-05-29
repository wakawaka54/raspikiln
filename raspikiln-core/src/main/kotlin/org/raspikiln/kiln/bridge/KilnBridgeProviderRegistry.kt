package org.raspikiln.kiln.bridge

fun kilnBridgeProviderRegistry(init: KilnBridgeProviderRegistry.() -> Unit) = KilnBridgeProviderRegistry().apply(init)

/**
 * Registry of kiln bridge providers..
 */
class KilnBridgeProviderRegistry {
    private val providers = mutableMapOf<String, KilnBridgeProvider>()

    fun register(provider: KilnBridgeProvider) = apply { providers.put(provider.name(), provider) }

    operator fun KilnBridgeProvider.unaryPlus() = register(this)

    fun find(name: String) = requireNotNull(providers[name]) { "Missing kiln bridge provider [$name]" }
}