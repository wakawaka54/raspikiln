package org.raspikiln.kiln.programs

/**
 * Defines a kiln program.
 */
interface Program {
    fun name(): String
    fun start()
    fun evaluate()
    fun end()
}