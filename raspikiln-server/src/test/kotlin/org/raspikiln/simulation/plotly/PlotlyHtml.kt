package org.raspikiln.simulation.plotly

import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.html.*
import org.raspikiln.core.Mapper

fun HEAD.plotlyCdn() =
    script {
        src =  "https://cdn.plot.ly/plotly-2.9.0.min.js"
    }

fun BODY.plotly(init: PlotlyBuilder.() -> Unit) = plotly(PlotlyBuilder().apply(init).build())

fun BODY.plotly(plot: Plotly) {
    plot.config.title?.let { title ->
        h4 { +title }
    }

    div {
        id = plot.config.id
        style = "width:600px;height:250px;"
    }

    script {
        unsafe {
            val id = plot.config.id
            val data = plot.series.toJSON()
            // language=js
            +"""
                const $id = document.getElementById('$id');
                Plotly.newPlot($id, $data, { margin: { t: 0 } });   
            """.trimIndent()
        }
    }
}

private fun objectMapper() = Mapper.jsonMapper().copy().setSerializationInclusion(JsonInclude.Include.NON_NULL);
private fun <T : Any> T.toJSON() = objectMapper().writeValueAsString(this)