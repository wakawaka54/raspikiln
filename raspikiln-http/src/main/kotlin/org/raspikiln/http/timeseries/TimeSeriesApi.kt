package org.raspikiln.http.timeseries

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import org.raspikiln.tsdb.TimesSeriesDB
import java.time.Duration
import java.time.Instant

fun Application.timeseriesApi() {
    val timeSeriesDB: TimesSeriesDB by inject()
    val timeSeriesQueryEngine by lazy { TimeSeriesQueryEngine(timeSeriesDB) }
    val queryResultConverter = TimeSeriesQueryResultConverter()

    routing {
        route("/v1/timeseries") {
            get {
                val query =
                    TimeSeriesQuery(
                        metricNames = call.request.queryParameters.metricNames(),
                        start = call.request.queryParameters.instant("start") {
                            Instant.now().minus(Duration.ofMinutes(15))
                        },
                        end = call.request.queryParameters.instant("end") { Instant.now() }
                    )

                call.respond(
                    queryResultConverter.convert(
                        query = query,
                        datapoints = timeSeriesQueryEngine.query(query)
                    )
                )
            }
        }
    }
}

private fun Parameters.metricNames() = requireNotNull(get("metricNames")).split(",")
private fun Parameters.instant(name: String, default: () -> Instant) = get(name)?.toLong()?.toInstant() ?: default()

private fun Long?.toInstant() = this?.let { Instant.ofEpochSecond(this) }