package org.raspikiln.http.core.controllers

import io.ktor.server.routing.*

interface Controller {
    fun Route.bind()
}

fun Route.controller(controller: () -> Controller) = with (controller()) { bind() }