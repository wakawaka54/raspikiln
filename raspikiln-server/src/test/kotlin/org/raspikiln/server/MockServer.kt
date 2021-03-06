package org.raspikiln.server

import mu.KotlinLogging
import org.koin.dsl.module
import org.raspikiln.kiln.KilnDefinitionProviderRegistration
import org.raspikiln.mock.MockKilnDefinitionProvider

private val logger = KotlinLogging.logger {  }

/**
 * Run the server with a mock RPI implementation.
 */
fun main(args: Array<String>) {
    logger.info { "Starting mock server..." }
    ServerCommand(koinAppFn = koinApp(MockServerModule)).main(args)
}

private val MockServerModule = module {
    single {
        KilnDefinitionProviderRegistration(
            name = "mock",
            provider = MockKilnDefinitionProvider()
        )
    }
}