package org.raspikiln.http.kiln

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.raspikiln.http.core.controllers.controller
import org.raspikiln.http.kiln.config.KilnConfigController
import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.config.Config
import org.raspikiln.kiln.config.http.HttpConfig

fun Application.kilnApi(config: HttpConfig) {
    val kiln: Kiln by inject()

    routing {
        route("/v1/kiln") {
            programs(kiln)
            currentState(kiln)
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

private fun Route.currentState(kiln: Kiln) = route("current") {
    get("state") {
        call.respond(kiln.state())
    }
}

private fun Route.dashboardConfig(config: HttpConfig) = route("config") {
    controller { KilnConfigController(config = config.dashboard) }
}