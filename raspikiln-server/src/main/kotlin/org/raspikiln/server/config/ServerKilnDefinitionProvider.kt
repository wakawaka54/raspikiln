package org.raspikiln.server.config

import org.koin.core.Koin
import org.raspikiln.core.raspikilnException
import org.raspikiln.kiln.KilnDefinition
import org.raspikiln.kiln.KilnDefinitionProvider
import org.raspikiln.kiln.config.KilnConfig

fun serverKilnProvider(init: MutableMap<String, KilnDefinitionProvider>.() -> Unit) =
    ServerKilnDefinitionProvider(mutableMapOf<String, KilnDefinitionProvider>().apply(init).toMap())

fun Koin.serverKilnProvider() = get<ServerKilnDefinitionProvider>()

/**
 * Finds the kiln provider referenced in the config.
 */
class ServerKilnDefinitionProvider(
    private val registrations: Map<String, KilnDefinitionProvider>
) : KilnDefinitionProvider {

    override fun create(config: KilnConfig): KilnDefinition = config.createKiln()

    private fun KilnConfig.createKiln() =
        registrations[provider]?.create(this) ?: raspikilnException(message = "Missing kiln provider $provider")
}