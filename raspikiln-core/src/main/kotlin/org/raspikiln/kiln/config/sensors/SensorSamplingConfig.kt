package org.raspikiln.kiln.config.sensors

import java.time.Duration

data class SensorSamplingConfig(
    val window: Duration,
    val samples: Int
)