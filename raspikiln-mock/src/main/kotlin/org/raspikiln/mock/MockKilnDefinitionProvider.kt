package org.raspikiln.mock

import org.raspikiln.kiln.KilnDefinition
import org.raspikiln.kiln.KilnDefinitionProvider
import org.raspikiln.kiln.config.KilnConfig

class MockKilnDefinitionProvider : KilnDefinitionProvider {
    override fun create(config: KilnConfig): KilnDefinition = MockKilnDefinition()
}