package org.raspikiln.server

import mu.KotlinLogging
import java.lang.Exception

private val logger = KotlinLogging.logger {  }

/**
 * Entrypoint of the RPI server.
 */
fun main(args: Array<String>) {
    try {
        Server().main(args)
    } catch (ex: Exception) {
        logger.error(ex) { "Raspikiln exited due to an unhandled exception!" }
    } finally {
        // do something.
    }
}