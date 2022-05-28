package org.raspikiln.kiln.programs

import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.programs.types.TemperatureSetpoint
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.zones.KilnZoneName

abstract class BaseProgram(
    private val name: String,
    protected val kiln: Kiln, options: BaseProgramOptions
) : Program {
    private val zones = options.zones.map { kiln.zone(it) }
    private val mutex = Any()

    override fun start(): Program = synchronized {
        zones.forEach { it.armingState(SwitchState.On) }
    }

    override fun end(): Program = synchronized {
        zones.forEach {
            it.target(TemperatureSetpoint.Unset)
        }
        zones.forEach {
            it.heaterState(SwitchState.Off)
            it.armingState(SwitchState.Off)
        }
    }

    protected fun synchronized(block: () -> Unit) = apply {
        synchronized(mutex) { block() }
    }
}

interface BaseProgramOptions {
    /**
     * Zones in the automatic program.
     */
    val zones: List<KilnZoneName>
}