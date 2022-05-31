package org.raspikiln.kiln.programs

import com.fasterxml.jackson.databind.ObjectMapper
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.controller.KilnControllerRegistry
import org.raspikiln.kiln.programs.manual.ManualProgram
import org.raspikiln.kiln.programs.manual.ManualProgramInput
import org.raspikiln.kiln.programs.types.StartProgramInput
import org.raspikiln.kiln.state.KilnState

/**
 * Loads a program.
 */
class ProgramFactory(
    private val controllers: KilnControllerRegistry,
    private val state: KilnState,
    private val mapper: ObjectMapper
) {

    fun create(definition: StartProgramInput): Program {
        return when (definition.name) {
            ManualProgram.Name -> manualProgram(definition)
            else -> error("Unrecognized program ${definition.name}")
        }
    }

    private fun manualProgram(definition: StartProgramInput): ManualProgram {
        val config: ManualProgramInput = mapper.treeToValue(definition.options, ManualProgramInput::class.java)
        return ManualProgram(
            controllers = config.controllers.map { controllers.requiredFind(it) },
            config = config
        )
    }

    /**
        when (definition.name) {
            /**
            ManualProgram.Name -> ManualProgram(
                kiln = kiln,
                options = Mapper.jsonMapper().treeToValue(definition.options, ManualProgramOptions::class.java)
            )
            **/
            else -> error("Unregonized program ${definition.name}")
        }
    **/
}