package org.raspikiln.kiln.programs.manual

import org.raspikiln.core.units.Temperature

data class ManualProgramInput(
    /**
     * Temperature to manually set.
     */
    val temperature: Temperature,

    /**
     * List of controllers to set the temperature on.
     */
    val controllers: List<String>
)