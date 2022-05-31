package org.raspikiln.http.kiln.config

import org.raspikiln.core.units.Temperature
import org.raspikiln.http.core.types.MetricInfo

/**
 * Config for the dashboard on the webapp.
 */
data class DashboardWebConfig(
    val programs: Programs,
    val chart: Chart
) {
    data class Programs(
        val manual: Manual,
        val automatic: List<Automatic>
    ) {
        data class Manual(
            val name: String,
            val controllers: List<String>
        )

        data class Automatic(
            val name: String,
            val steps: List<Step>
        ) {
            data class Step(
                val temperature: Temperature,
                val ramp: Temperature,
                val controller: String
            )
        }
    }

    data class Chart(
        val temperatureMetrics: List<MetricInfo>,
        val targetMetrics: List<MetricInfo>
    )
}