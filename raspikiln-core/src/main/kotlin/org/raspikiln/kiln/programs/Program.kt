package org.raspikiln.kiln.programs

/**
 * Defines a kiln program.
 */
interface Program {
    fun name(): String
    fun start(): Program
    fun evaluate(): Program
    fun end(): Program
}