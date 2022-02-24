package org.raspikiln.kiln.zones

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.switches.SwitchState

data class ZoneState(
    val name : KilnZoneName,
    val temperature: Temperature,
    val heaterSwitchState: SwitchState,
    val armingSwitchState: SwitchState,
    val temperatureSetpoint: Temperature?
)