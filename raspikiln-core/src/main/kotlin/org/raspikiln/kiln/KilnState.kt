package org.raspikiln.kiln

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.switches.SwitchState

/**
 * Kiln state information.
 */
data class KilnState(
    val armState: SwitchState,
    val temperature: Temperature,
    val temperatureTarget: Temperature?
)