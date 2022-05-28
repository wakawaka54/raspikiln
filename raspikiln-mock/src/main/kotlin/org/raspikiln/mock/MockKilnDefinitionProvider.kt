package org.raspikiln.mock

import org.raspikiln.kiln.KilnBridge
import org.raspikiln.kiln.KilnBridgeProvider
import org.raspikiln.kiln.config.KilnConfig

class MockKilnDefinitionProvider : KilnBridgeProvider {
    override fun create(config: KilnConfig): KilnBridge = MockKilnDefinition()
}