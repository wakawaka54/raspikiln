package org.raspikiln.server

import mu.KotlinLogging
import org.raspikiln.http.KilnHttp
import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.config.HttpConfig
import org.raspikiln.kiln.config.KilnConfigDefinition

private val logger = KotlinLogging.logger {  }

class KilnApplication(
    private val kilnConfigDefinition: KilnConfigDefinition,
    private val kiln: Kiln
) {

    fun run() {
        kiln.start()

        with (kilnConfigDefinition) {
            http.maybeStartHttp()
        }
    }

    private fun HttpConfig.maybeStartHttp() {
        if (!enabled) { return }

        logger.info { "Starting HTTP API on $port" }

        KilnHttp.create(this).start()
    }
}