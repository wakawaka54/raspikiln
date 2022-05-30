package org.raspikiln.kiln.state

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.switches.SwitchState
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class KilnState {
    private val switchState = ConcurrentHashMap<String, SwitchState>()
    private val temperatureState = ConcurrentHashMap<String, Temperature>()

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