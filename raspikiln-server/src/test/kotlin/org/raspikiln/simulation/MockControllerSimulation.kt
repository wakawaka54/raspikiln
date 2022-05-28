package org.raspikiln.simulation

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.html.BODY
import mu.KotlinLogging
import org.raspikiln.core.Mapper
import org.raspikiln.core.files.makeDirectories
import org.raspikiln.core.files.makeParentDirectories
import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.controller.algorithm.TupleTerms
import org.raspikiln.simulation.ControlSimulation.Options
import org.raspikiln.simulation.plotly.plotly
import org.raspikiln.simulation.runner.ControlSimulationRunner
import org.raspikiln.simulation.runner.SimulationRunnerOptions
import java.io.File
import java.time.Instant
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private val logger = KotlinLogging.logger {  }

fun main(args: Array<String>) {
    MockControllerSimulation().main(args)
}

/**
 * Run a controller simulation.
 */
class MockControllerSimulation : CliktCommand(help = "Run a mock controller simulation.") {

    private val inputFile by option("--input", "-i")
        .file()
        .default(File("data/simulation/input.yml"))
        .validate { it.makeParentDirectories() }

    private val outputDirectory by option("--output", "-o")
        .file()
        .default(File("data/simulation"))
        .validate { it.makeDirectories() }

    private val runner by lazy { ControlSimulationRunner(outputDirectory) }

    override fun run() {
        logger.info { "Running control simulation..." }

        simulationResultsServer(outputDirectory)

        do {
            runner.run(
                inputOptions() ?: defaultOptions()
            )
        }  while (TermUi.confirm("Run another simulation?") == true)
    }

    private fun inputOptions() =
        inputFile.takeIf { it.exists() }
            ?.let { SimMapper.Yaml.readValue(it, SimulationRunnerOptions::class.java) }

    private fun defaultOptions() =
        SimulationRunnerOptions().apply {
            inputFile.writeText(SimMapper.Yaml.writeValueAsString(this))
        }
}