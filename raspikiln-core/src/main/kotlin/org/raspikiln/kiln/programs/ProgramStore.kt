package org.raspikiln.kiln.programs

import org.raspikiln.kiln.config.programs.ProgramConfig

/**
 * Stores programs for the kiln.
 */
class ProgramStore(private val config: List<ProgramConfig>) {
    fun readAll(): List<ProgramConfig> = config
}