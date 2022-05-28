package org.raspikiln.dashboard

import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

fun main(args: Array<String>) {
    Server().run()
}

class Server {
    fun run() {
        embeddedServer(Netty, port = 8000, host = "0.0.0.0") {
            routing {
                singlePageApplication {
                    useResources = true
                    filesPath = "webapp"
                    defaultPage = "index.html"
                }
            }
        }
        .start(wait = true)
    }
}