package org.raspikiln.kiln.historical

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.KilnState
import org.raspikiln.kiln.switches.SwitchState
import java.time.ZonedDateTime

/**
 * Historical kiln information.
 */
interface KilnHistoricalStore {

    /**
     * Record a kiln state.
     */
    fun record(state: KilnState)
}