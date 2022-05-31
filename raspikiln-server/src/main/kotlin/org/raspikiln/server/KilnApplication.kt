package org.raspikiln.server

import mu.KotlinLogging
import org.raspikiln.http.KilnHttp
import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.KilnFactory
import org.raspikiln.kiln.config.Config
import org.raspikiln.kiln.config.http.HttpConfig

private val logger = KotlinLogging.logger {  }

class KilnApplication(
    private val kilnFactory: KilnFactory
) {
    fun start(config: Config) {
        val kiln = kilnFactory.create(config.kiln)

        kiln.start()

        if (config.http.enabled) {
            startHttp(kiln, config)
        }
    }

    private fun startHttp(kiln: Kiln, config: Config) {
        val http = config.http
        logger.info { "Starting HTTP API on ${http.port}" }
        KilnHttp.create(http, kiln).start()
    }
}