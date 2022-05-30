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

        with (config) {
            http.maybeStartHttp()
        }
    }

    private fun HttpConfig.maybeStartHttp() {
        if (!enabled) { return }

        logger.info { "Starting HTTP API on $port" }

        KilnHttp.create(this).start()
    }
}