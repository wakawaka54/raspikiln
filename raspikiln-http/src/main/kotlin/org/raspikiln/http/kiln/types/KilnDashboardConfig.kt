package org.raspikiln.http.kiln.types

import org.raspikiln.http.core.types.MetricInfo
import org.raspikiln.kiln.zones.KilnZoneName

/**
 * Kiln definition.
 */
data class KilnDashboardConfig(
    val zones: List<KilnZoneName>,
    val chart: DashboardChart
) {
    data class DashboardChart(
        val temperatureMetrics: List<MetricInfo>,
        val targetMetrics: List<MetricInfo>,
    )
}