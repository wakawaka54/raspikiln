package org.raspikiln.kiln.initialization

import org.raspikiln.kiln.bridge.KilnBridge

/**
 * Initializer for the kiln.
 */
class KilnInitializer {
    fun initialize(bridge: KilnBridge): KilnInitialization =
        KilnInitializationBuilder().apply { bridge.initialize(this) }.build()
}