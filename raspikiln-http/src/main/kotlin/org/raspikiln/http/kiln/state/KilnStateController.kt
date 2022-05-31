package org.raspikiln.http.kiln.state

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.raspikiln.http.core.controllers.Controller
import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.config.http.DashboardConfig

class KilnStateController(
    private val config: DashboardConfig,
    private val kiln: Kiln
): Controller {

    override fun Route.bind() {
        route("current") {
            get("state") {
                call.respond(getKilnStatus())
            }
        }
    }

    private fun getKilnStatus(): KilnStatus {
        val state = kiln.state()
        return KilnStatus(
            temperature = state.requiredTemperature(config.temperatureMetric),
            temperatureTarget = state.temperature(config.targetTemperatureMetric),
            armState = state.requiredSwitchState(config.heaterSwitchMetric)
        )
    }
}