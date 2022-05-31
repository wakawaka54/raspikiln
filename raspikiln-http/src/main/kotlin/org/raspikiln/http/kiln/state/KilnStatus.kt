package org.raspikiln.http.kiln.state

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.switches.SwitchState

data class KilnStatus(
    val temperature: Temperature,
    val temperatureTarget: Temperature?,
    val armState: SwitchState
)