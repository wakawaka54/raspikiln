package org.raspikiln.mock

import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.bridge.KilnBridgeProvider
import org.raspikiln.kiln.config.KilnConfig

class MockKilnBridgeProvider : KilnBridgeProvider {
    override fun name(): String = "mock"
    override fun create(config: KilnConfig): KilnBridge = MockKilnBridge()
}