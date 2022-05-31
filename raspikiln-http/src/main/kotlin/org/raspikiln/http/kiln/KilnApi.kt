package org.raspikiln.http.kiln

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.raspikiln.http.core.controllers.controller
import org.raspikiln.http.kiln.config.KilnConfigController
import org.raspikiln.http.kiln.state.KilnStateController
import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.config.Config
import org.raspikiln.kiln.config.http.HttpConfig

fun Application.kilnApi(kiln: Kiln, config: HttpConfig) {
    routing {
        route("/v1/kiln") {
            programs(kiln)
            state(kiln, config)
            dashboardConfig(config)
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

private fun Route.state(kiln: Kiln, config: HttpConfig) {
    controller { KilnStateController(config.dashboard, kiln) }
}

private fun Route.dashboardConfig(config: HttpConfig) = route("config") {
    controller { KilnConfigController(config = config.dashboard) }
}