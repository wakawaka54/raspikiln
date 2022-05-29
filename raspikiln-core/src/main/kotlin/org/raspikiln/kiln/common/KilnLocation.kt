package org.raspikiln.kiln.common

/**
 * Location in the kiln.
 */
data class KilnLocation(val name: String) {
    companion object {
        val Oven = KilnLocation(name = "oven")
    }
}