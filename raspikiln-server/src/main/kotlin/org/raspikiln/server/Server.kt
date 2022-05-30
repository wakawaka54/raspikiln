package org.raspikiln.server

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import mu.KotlinLogging
import org.raspikiln.server.config.ConfigFileReader

private val logger = KotlinLogging.logger { }

class Server(private val app: AppComponent) : CliktCommand(
    name = "raspikiln",
    help = "Launch raspikiln application."
) {

    private val configFile by option("--config", "-c").file(mustExist = true, mustBeReadable = true).required()

    private val configFileReader = ConfigFileReader()

    override fun run() {
        logger.info { "Starting raspikiln..." }
        logger.info { "Kiln definition is\n${configFile.readText()}" }

        app.kilnApplication().start(config = configFileReader.read(configFile))

        waitUntilStopped()
    }

    private fun waitUntilStopped() {
        while (true) {
            Thread.sleep(1000)
        }
    }
}