package org.raspikiln.kiln.programs

import org.raspikiln.core.Mapper
import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.programs.types.ProgramDefinition

/**
 * Loads a program.
 */
class ProgramFactory {
    fun create(kiln: Kiln, definition: ProgramDefinition): Program =
        when (definition.name) {
            /**
            ManualProgram.Name -> ManualProgram(
                kiln = kiln,
                options = Mapper.jsonMapper().treeToValue(definition.options, ManualProgramOptions::class.java)
            )
            **/
            else -> error("Unregonized program ${definition.name}")
        }
}