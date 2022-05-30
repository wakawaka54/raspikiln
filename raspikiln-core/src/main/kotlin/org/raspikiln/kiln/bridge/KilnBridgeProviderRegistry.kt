package org.raspikiln.kiln.bridge

/**
 * Registry of kiln bridge providers..
 */
class KilnBridgeProviderRegistry {
    private val providers = mutableMapOf<String, KilnBridgeProvider>()

    fun registerAll(providers: List<KilnBridgeProvider>) = apply { providers.forEach { register(it) } }

    fun register(provider: KilnBridgeProvider) = apply { providers.put(provider.name(), provider) }

    operator fun KilnBridgeProvider.unaryPlus() = register(this)

    fun find(name: String) = requireNotNull(providers[name]) { "Missing kiln bridge provider [$name]" }
}