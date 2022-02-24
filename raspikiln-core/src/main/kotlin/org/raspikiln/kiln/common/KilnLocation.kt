package org.raspikiln.kiln.common

/**
 * Location on a kiln.
 */
sealed interface KilnLocation {

    /**
     * Kiln oven.
     */
    object Oven : KilnLocation {
        override fun name(): String = "oven"
    }

    fun name(): String
}