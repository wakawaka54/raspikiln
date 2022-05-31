package org.raspikiln.kiln.programs.manual

import org.raspikiln.kiln.controller.TemperatureController
import org.raspikiln.kiln.programs.Program
import org.raspikiln.kiln.programs.types.TemperatureSetpoint
import org.raspikiln.kiln.state.KilnState

/**
 * A manual problem.
 */
class ManualProgram(
    private val controllers: List<TemperatureController>,
    private val config: ManualProgramInput
) : Program {
    companion object {
        const val Name = "manual"
    }

    override fun name(): String = Name

    override fun start() {
        controllers.forEach { it.set(TemperatureSetpoint.Value(temperature = config.temperature)) }
    }

    override fun evaluate() {
        // do nothing.
    }

    override fun end() {
        controllers.forEach { it.unset() }
    }
}