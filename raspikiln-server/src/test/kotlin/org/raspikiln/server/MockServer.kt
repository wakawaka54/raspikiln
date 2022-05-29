package org.raspikiln.server

import mu.KotlinLogging
import org.raspikiln.kiln.bridge.kilnBridgeProviderRegistry
import org.raspikiln.mock.MockKilnBridgeProvider

private val logger = KotlinLogging.logger {  }

/**
 * Run the server with a mock RPI implementation.
 */
fun main(args: Array<String>) {
    logger.info { "Starting mock server..." }
    Server(
        appComponentFactory = AppComponentFactory.create(
            bridgeRegistry = kilnBridgeProviderRegistry {
                +MockKilnBridgeProvider()
            }
        )
    ).main(args)
}