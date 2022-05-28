package org.raspikiln.http.kiln

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.raspikiln.http.core.strings.strings
import org.raspikiln.http.core.types.MetricInfo
import org.raspikiln.http.kiln.types.KilnDashboardConfig
import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.common.MetricName
import org.raspikiln.kiln.zoneNames

fun Application.kilnApi() {
    val kiln: Kiln by inject()

    routing {
        route("/v1/kiln") {
            programs(kiln)
            currentState(kiln)
            dashboardConfig(kiln)
        }
    }
}

private fun Route.programs(kiln: Kiln) = route("programs") {
    post("start") {
        kiln.startProgram(definition = call.receive())
        call.respond(kiln.state())
    }

    post("stop") {
        kiln.endProgram()
        call.respond(kiln.state())
    }
}

private fun Route.currentState(kiln: Kiln) = route("current") {
    get("state") {
        call.respond(kiln.state())
    }
}

private fun Route.dashboardConfig(kiln: Kiln) = route("config") {
    get {
        call.respond(
            KilnDashboardConfig(
                zones = kiln.zones().map { it.name },
                chart = KilnDashboardConfig.DashboardChart(
                    temperatureMetrics = kiln.zoneNames().map { zone ->
                        MetricInfo(
                            name = strings().temperatureMetricDisplayName(kiln, zone).value(),
                            metricName = MetricName.temperature(zone).name
                        )
                    },
                    targetMetrics = kiln.zoneNames().map { zone ->
                        MetricInfo(
                            name = strings().temperatureTargetMetricDisplayName(kiln, zone).value(),
                            metricName = MetricName.temperatureTarget(zone).name
                        )
                    },
                )
            )
        )
    }
}