package org.raspikiln.server

import mu.KotlinLogging
import org.koin.dsl.module
import org.raspikiln.kiln.bridge.KilnBridgeProvider
import org.raspikiln.mock.MockKilnBridgeProvider

private val logger = KotlinLogging.logger {  }

/**
 * Run the server with a mock RPI implementation.
 */
fun main(args: Array<String>) {
    logger.info { "Starting mock server..." }
    Server(app = AppComponent.create(AppModule + MockModule)).main(args)
}

private val MockModule = module { single<KilnBridgeProvider> { MockKilnBridgeProvider() } }