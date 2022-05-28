package org.raspikiln.http.health

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.healthApi() {
    routing {
        get("/ping") {
            call.respondText("http server ok")
        }
    }
}