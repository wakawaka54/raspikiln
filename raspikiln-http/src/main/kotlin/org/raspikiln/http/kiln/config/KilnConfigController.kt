package org.raspikiln.http.kiln.config

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.raspikiln.http.core.controllers.Controller
import org.raspikiln.http.core.types.MetricInfo
import org.raspikiln.kiln.config.http.DashboardConfig

class KilnConfigController(
    private val config: DashboardConfig
) : Controller {
    override fun Route.bind() {
        get { call.respond(getDashboardConfig()) }
    }

    private fun getDashboardConfig() =
        DashboardWebConfig(
            programs = DashboardWebConfig.Programs(
                manual = DashboardWebConfig.Programs.Manual(
                    name = "test",
                    controllers = emptyList()
                ),
                automatic = emptyList()
            ),
            chart = DashboardWebConfig.Chart(
                temperatureMetrics = config.temperatureMetrics.map { MetricInfo(name = it.display, metricName = it.metric) },
                targetMetrics = config.targetTemperatureMetrics.map { MetricInfo(name = it.display, metricName = it.metric) }
            )
        )
}