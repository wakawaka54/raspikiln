package org.raspikiln.kiln.state

import org.raspikiln.core.time.SystemClock
import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.switches.SwitchState
import java.time.Clock
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.math.exp
import kotlin.time.Duration

class KilnState(private val clock: SystemClock) {
    private val switchState = ConcurrentHashMap<String, SwitchState>()
    private val temperatureState = ConcurrentHashMap<String, Temperature>()

    fun timeNow(): Instant = clock.instant()

    fun durationTime(expected: Duration): Duration = clock.duration(expected)

    fun requiredTemperature(metric: String) = requireNotNull(temperature(metric)) { "Missing required temperature metric [$metric]" }
    fun temperature(metric: String) = temperatureState[metric]

    fun requiredSwitchState(metric: String) = requireNotNull(switchState(metric)) { "Missing required switch state metric [$metric]" }
    fun switchState(metric: String) = switchState[metric]

    fun update(metric: String, state: SwitchState) {
        switchState.update(metric, state)
    }

    fun update(metric: String, state: Temperature) {
        temperatureState.update(metric, state)
    }

    private fun <V> ConcurrentMap<String, V>.update(metric: String, state: V) {
        put(metric, state)
    }
}