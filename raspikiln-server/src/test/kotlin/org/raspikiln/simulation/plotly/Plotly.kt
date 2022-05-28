package org.raspikiln.simulation.plotly

import com.fasterxml.jackson.annotation.JsonValue
import org.apache.commons.lang3.RandomStringUtils

data class Plotly(
    val config: PlotlyConfig,
    val series: List<PlotlySeries>
)

data class PlotlyConfig(
    val id: String = RandomStringUtils.randomAlphabetic(8),
    val title: String? = null
)

data class PlotlySeries(
    val name: String,
    val x: List<Any>,
    val y: List<Double>,
    val mode: ChartMode,
    val line: Line? = null
) {
    enum class ChartMode {
        Lines;
        @JsonValue
        fun toJsonString() = name.lowercase()
    }

    data class Line(
        val dash: LineDash,
        val width: Int,
    )

    enum class LineDash {
        Solid, Dot;
        @JsonValue
        fun toJsonString() = name.lowercase()
    }
}