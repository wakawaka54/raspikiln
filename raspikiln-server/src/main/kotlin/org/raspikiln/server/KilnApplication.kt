package org.raspikiln.server

import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.raspikiln.http.KilnHttp
import org.raspikiln.kiln.config.HttpConfig
import org.raspikiln.kiln.config.KilnConfigDefinition

private val logger = KotlinLogging.logger {  }

class KilnApplication : KoinComponent {
    private val kilnConfigDefinition: KilnConfigDefinition by inject()

    fun run() {
        with (kilnConfigDefinition) {
            http.startHttp()
        }
    }

    private fun HttpConfig.startHttp() {
        if (!enabled) { return }

        logger.info { "Starting HTTP API on $port" }

        KilnHttp.create(this).start()
    }
}