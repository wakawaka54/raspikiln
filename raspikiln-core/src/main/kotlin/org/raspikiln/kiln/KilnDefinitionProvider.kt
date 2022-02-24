package org.raspikiln.kiln

import org.raspikiln.kiln.config.KilnConfig

/**
 * Provides a kiln definition.
 */
interface KilnDefinitionProvider {
    fun create(config: KilnConfig): KilnDefinition
}

/**
 * Registration of a kiln definition provider.
 */
data class KilnDefinitionProviderRegistration(
    val name: String,
    val provider: KilnDefinitionProvider
)