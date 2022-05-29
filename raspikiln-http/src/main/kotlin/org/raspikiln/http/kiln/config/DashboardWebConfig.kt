package org.raspikiln.http.kiln.config

import org.raspikiln.http.core.types.MetricInfo

/**
 * Config for the dashboard on the webapp.
 */
data class DashboardWebConfig(
    val metrics: List<MetricInfo>
)