package org.raspikiln.kiln.initialization

import org.raspikiln.kiln.bridge.KilnBridge

/**
 * Initializer for the kiln.
 */
interface KilnInitializer {
    fun initialize(builder: KilnInitializationBuilder)
}