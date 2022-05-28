package org.raspikiln.simulation

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.html.BODY
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import mu.KotlinLogging
import org.raspikiln.core.units.Temperature
import org.raspikiln.simulation.plotly.plotly
import org.raspikiln.simulation.plotly.plotlyCdn
import org.raspikiln.simulation.runner.SimulationResultData
import org.raspikiln.simulation.runner.SimulationResults
import java.io.File
import kotlin.time.Duration

private val logger = KotlinLogging.logger {  }

/**
 * Serves simulation results.
 */
fun simulationResultsServer(resultsDirectory: File) =
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                call.respondHtml {
                    head {
                        plotlyCdn()
                    }
                    body {
                        try {
                            SimulationResults.loadAll(resultsDirectory)
                                .map { it.readData() }
                                .sortedByDescending { it.creationTime }
                                .forEach { results -> plot(results) }
                        } catch (ex: Exception) {
                            logger.error(ex) { "Failed to create plots." }
                        }
                    }
                }
            }
        }
    }
    .start(wait = false)

private fun BODY.plot(resultData: SimulationResultData) =
    plotly {
        title = "${resultData.creationTime} ${resultData.options.gainTerms}"
        line {
            name = "temperature"
            x = resultData.states.map { it.timestamp.toString() }
            y = resultData.states.map { it.temperature }
        }

        line {
            name = "target"
            x = resultData.states.map { it.timestamp.toString() }
            y = resultData.states.map { it.setpoint }
        }
    }