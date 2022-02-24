package org.raspikiln.kiln.programs

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.programs.types.TemperatureSetpoint
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.zones.KilnZoneName

class ManualProgram(
    private val kiln: Kiln,
    private val options: ManualProgramOptions
) : Program {
    companion object {
        const val Name = "manual"
    }

    private val zones = options.zones.map { kiln.zone(it) }
    private val mutex = Any()

    override fun name(): String = Name

    override fun start() = synchronized {
        zones.forEach { it.armingState(SwitchState.On) }
    }

    override fun evaluate() = synchronized {
        zones.forEach {
            it.target(
                TemperatureSetpoint.Value(
                    temperature = options.temperature
                )
            )
        }
    }

    override fun end() = synchronized {
        zones.forEach {
            it.target(TemperatureSetpoint.Unset)
        }
        zones.forEach {
            it.heaterState(SwitchState.Off)
            it.armingState(SwitchState.Off)
        }
    }

    private fun synchronized(block: () -> Unit) = apply {
        synchronized(mutex) { block() }
    }
}

data class ManualProgramOptions(

    /**
     * Zones in the manual program.
     */
    val zones: List<KilnZoneName>,

    /**
     * Set point of the manual program.
     */
    val temperature: Temperature
)