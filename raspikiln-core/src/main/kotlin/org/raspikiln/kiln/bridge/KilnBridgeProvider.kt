package org.raspikiln.kiln.bridge

import org.raspikiln.kiln.config.KilnConfig

/**
 * Provides a kiln bridge.
 */
interface KilnBridgeProvider {
    fun name(): String
    fun create(config: KilnConfig): KilnBridge
}