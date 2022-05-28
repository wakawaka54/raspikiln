package org.raspikiln.http

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.raspikiln.core.registerCoreModule
import org.raspikiln.http.health.healthApi
import org.raspikiln.http.kiln.jackson.registerKilnApiModule
import org.raspikiln.http.kiln.kilnApi
import org.raspikiln.http.timeseries.timeseriesApi
import org.raspikiln.kiln.config.HttpConfig
import org.slf4j.event.Level

/**
 * HTTP API for accessing the Kiln.
 */
class KilnHttp(private val application: ApplicationEngine) {
    companion object {
        fun create(config: HttpConfig) = KilnHttp(
            embeddedServer(Netty, port = config.port) {
                install(CallLogging) {
                    level = Level.WARN
                }
                install(CORS) {
                    allowHeader(HttpHeaders.ContentType)

                    anyHost()
                }
                install(ContentNegotiation) {
                    jackson {
                        registerKotlinModule()
                        registerCoreModule()
                        registerKilnApiModule()
                        registerModule(JavaTimeModule())
                    }
                }
                install(StatusPages) {
                    exception { call: ApplicationCall, cause: Throwable ->
                        call.respond(
                            status = HttpStatusCode.InternalServerError,
                            message = mapOf(
                                "stackTrace" to cause.stackTraceToString()
                            )
                        )
                    }
                }

                healthApi()
                kilnApi()
                timeseriesApi()
            }
        )
    }

    fun start() {
        application.start(wait = false)
    }
}