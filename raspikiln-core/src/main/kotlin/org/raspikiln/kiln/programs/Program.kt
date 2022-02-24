package org.raspikiln.kiln.programs

import org.raspikiln.kiln.Kiln

/**
 * Defines a kiln program.
 */
interface Program {
    fun name(): String
    fun start(): Program
    fun evaluate(): Program
    fun end(): Program
}