package org.raspikiln.kiln.config.http

data class HttpConfig(
    /**
     * Whether to enable the HTTP server.
     */
    val enabled: Boolean,

    /**
     * The port to enable the HTTP server on.
     */
    val port: Int,

    /**
     * Dashboard configuration.
     */
    val dashboard: DashboardConfig
)