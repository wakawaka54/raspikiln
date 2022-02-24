package org.raspikiln.tsdb

import java.time.Instant
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class Measurement(
    val metric: MetricIdentifier,
    val datapoint: Datapoint
)

data class MetricIdentifier(
    val name: String,
    val labels: Map<String, String> = emptyMap()
) {
    override fun toString(): String =
        buildString(name.length + labels.entries.sumOf { it.key.length + it.value.length }) {
            append(name)
            labels.forEach { (key, value) ->
                append(key)
                append(value)
            }
        }
}

data class Datapoint(
    val timestamp: Instant = Instant.now(),
    val value: Double
)