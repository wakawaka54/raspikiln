package org.raspikiln.kiln.config.http

data class DashboardConfig(

    /**
     * Metric which represents the heater switch state.
     */
    val heaterSwitchMetric: String,

    /**
     * Metric which represents the oven temperature.
     */
    val temperatureMetric: String,

    /**
     * Metric which represents the target oven temperature.
     */
    val targetTemperatureMetric: String,

    /**
     * Temperature metrics to display on the dashboard.
     */
    val temperatureMetrics: List<DisplayMetricConfig>,

    /**
     * Metrics to display on the dashboard.
     */
    val targetTemperatureMetrics: List<DisplayMetricConfig>
)