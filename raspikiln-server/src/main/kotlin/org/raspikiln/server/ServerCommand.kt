package org.raspikiln.server

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import mu.KotlinLogging
import org.koin.core.Koin
import org.koin.dsl.koinApplication
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.kiln.config.KilnConfigDefinition
import org.raspikiln.server.config.ConfigFileReader
import org.raspikiln.server.config.serverKilnProvider
import org.raspikiln.server.core.startKoinApplication

private val logger = KotlinLogging.logger { }

class ServerCommand(private val koinAppFn: (KilnConfigDefinition) -> Koin = koinApp()) : CliktCommand(
    name = "raspikiln",
    help =
    """
        Launch Raspikiln.    
    """.trimIndent()
) {
    private val configFile by
        option("--config", "-c")
            .file(mustExist = true, mustBeReadable = true)
            .required()

    private val configFileReader = ConfigFileReader()

    override fun run() {
        logger.info { "Starting raspikiln..." }
        logger.info { "Kiln definition is\n${configFile.readText()}" }

        val kilnConfig = configFileReader.read(configFile)

        koinAppFn(kilnConfig).kilnStart()
    }

    private fun Koin.kilnStart() {
        val kilnApp: KilnApplication by inject()

        kilnApp.run()

        while (true) {
            Thread.sleep(1000)
        }
    }
}