package org.raspikiln.rpi

import mu.KotlinLogging
import org.raspikiln.kiln.config.switches.SwitchConfig
import org.raspikiln.kiln.kilnProviderError
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.switches.SwitchType
import org.raspikiln.rpi.components.DigitalSwitch
import org.raspikiln.rpi.core.PiContext
import org.raspikiln.rpi.core.digitalOutput

private val logger = KotlinLogging.logger { }

class RpiSwitchProvider(private val pi4j: PiContext) {
    fun create(switchConfig: SwitchConfig): Switch =
        when (switchConfig) {
            is SwitchConfig.DigitalSwitch -> switchConfig.create().print()
            else -> kilnProviderError(message = "Unknown switch type ${switchConfig::class}")
        }

    private fun SwitchConfig.DigitalSwitch.create() = DigitalSwitch(
        name = name,
        switchType = SwitchType.ArmingSwitch,
        metric = metric,
        digitalOutput = pi4j.digitalOutput {
            address(digitalOutput.pin)
        }
    )

    private fun DigitalSwitch.print() = apply {
        logger.info { "Bound digital output [${name()}] at pin [${pin()}]" }
    }
}