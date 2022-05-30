package org.raspikiln.kiln

import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.initialization.KilnInitializationFactory
import org.raspikiln.kiln.state.KilnStateService

class KilnModule {
    fun initializationFactory() = KilnInitializationFactory()
    fun stateService(bridge: KilnBridge) = KilnStateService(bridge)
}