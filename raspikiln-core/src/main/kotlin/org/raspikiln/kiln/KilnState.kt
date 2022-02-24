package org.raspikiln.kiln

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.zones.ZoneState

/**
 * Kiln state information.
 */
data class KilnState(
    val zones: List<ZoneState>,
    val armState: SwitchState,
    val temperature: Temperature,
    val temperatureTarget: Temperature?
)