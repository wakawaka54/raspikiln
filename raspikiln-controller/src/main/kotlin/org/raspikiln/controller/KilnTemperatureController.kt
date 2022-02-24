package org.raspikiln.controller

import mu.KotlinLogging
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.zones.KilnZone

private val logger = KotlinLogging.logger { }

/**
 * Kiln Temperature controller.
 */
class KilnTemperatureController {
    fun evaluate(zone: KilnZone) {
        val setpoint = zone.target() ?: return
        val state = zone.state()

        if (state.temperature > setpoint) {
            zone.heaterState(SwitchState.Off)
        } else {
            zone.heaterState(SwitchState.On)
        }
    }
}