package org.raspikiln.http.timeseries

import java.math.BigDecimal

/**
 * Convert query results to a slimmer response type.
 */
class TimeSeriesQueryResultConverter {
    fun convert(query: TimeSeriesQuery, datapoints: List<QueryDatapoint>): QueryResults =
        QueryResults(
            series = series(query),
            points = datapoints.map { pointValues(query, it) }.toList()
        )

    private fun series(query: TimeSeriesQuery) =
        query.metricNames.map { QueryResults.Series(name = it) }.toList()

    private fun pointValues(query: TimeSeriesQuery, datapoint: QueryDatapoint): PointValues =
        listOf(
            BigDecimal.valueOf(datapoint.interval.start.epochSecond)
        ) + query.metricNames.map { datapoint[it]?.toBigDecimal() }.toList()
}

data class QueryResults(
    val series: List<Series>,
    val points: List<PointValues>
) {
    data class Series(
        val name: String
    )
}

typealias PointValues = List<BigDecimal?>