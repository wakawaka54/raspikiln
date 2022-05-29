package org.raspikiln.kiln.config.http

data class DashboardConfig(

    /**
     * Metrics to display on the dashboard.
     */
    val metrics: List<DisplayMetricConfig>
)