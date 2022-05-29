package org.raspikiln.http.kiln.config

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.raspikiln.http.core.controllers.Controller
import org.raspikiln.http.core.types.MetricInfo
import org.raspikiln.kiln.config.http.DashboardConfig

class KilnConfigController(private val config: DashboardConfig) : Controller {
    override fun Route.bind() {
        get { call.respond(getDashboardConfig()) }
    }

    private fun getDashboardConfig() =
        DashboardWebConfig(
            metrics = config.metrics.map { MetricInfo(name = it.display, metricName = it.metric) }
        )
}