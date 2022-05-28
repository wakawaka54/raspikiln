package org.raspikiln.simulation.plotly

@DslMarker
annotation class PlotlyDsl

@PlotlyDsl
class PlotlyBuilder {
    private val series = mutableListOf<PlotlySeries>()

    var title: String? = null

    fun line(init: PlotlySeriesBuilder.() -> Unit) {
        series.add(
            PlotlySeriesBuilder(
                name = "plot ${series.size}",
                mode = PlotlySeries.ChartMode.Lines
            ).apply(init).build()
        )
    }

    fun build() =
        Plotly(
            config = PlotlyConfig(
                title = title
            ),
            series = series
        )
}

@PlotlyDsl
class PlotlySeriesBuilder(
    var name: String,
    var mode: PlotlySeries.ChartMode
) {
    var x: List<Any> = emptyList()
    var y: List<Double> = emptyList()
    var line: PlotlySeries.Line? = null

    fun build() = PlotlySeries(name = name, x = x, y = y, mode = mode, line = line)
}