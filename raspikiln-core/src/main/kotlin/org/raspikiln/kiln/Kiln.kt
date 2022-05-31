package org.raspikiln.kiln

import mu.KotlinLogging
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.controller.KilnControllerRegistry
import org.raspikiln.kiln.programs.*
import org.raspikiln.kiln.programs.types.StartProgramInput
import org.raspikiln.kiln.state.KilnState
import java.util.concurrent.atomic.AtomicBoolean

private val logger = KotlinLogging.logger {  }

/**
 * Kiln abstraction.
 */
class Kiln(
    private val bridge: KilnBridge,
    private val state: KilnState,
    private val programs: KilnProgramManager,
) {
    private val started = AtomicBoolean(false)

    fun start() { }

    /**
     * Get the current state of the kiln.
     */
    fun state(): KilnState = state

    fun programs() = programs

    fun startProgram(definition: StartProgramInput) {
        /**
        logger.info { "Starting program [definition]" }
        val program = programFactory.create(kiln = this, definition)
        this.program = program.start()
        **/
    }

    fun endProgram() {
        /**
        program?.end()
        program = null
        **/
    }

    /**
     * Current executing program.
     */
    // fun currentProgram(): Program? = program

    /**
     * The kiln definition.
     */
    fun definition() = bridge
}