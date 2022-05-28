package org.raspikiln.server

import org.raspikiln.kiln.KilnDefinitionProviderRegistration
import org.raspikiln.rpi.RpiKilnDefinitionProvider

object KilnDefinitionProviders {
    fun raspberryPi() =
        KilnDefinitionProviderRegistration(
            name = "rpi",
            provider = RpiKilnDefinitionProvider()
        )
}