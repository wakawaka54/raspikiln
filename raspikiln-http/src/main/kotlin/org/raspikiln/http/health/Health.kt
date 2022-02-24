package org.raspikiln.http.health

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.healthApi() {
    routing {
        get("/ping") {
            call.respondText("http server ok")
        }
    }
}