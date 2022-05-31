package org.raspikiln.rpi

import com.pi4j.io.spi.Spi
import mu.KotlinLogging
import org.raspikiln.kiln.config.sensors.SensorChipsetConfig
import org.raspikiln.kiln.config.sensors.SensorProtocolConfig
import org.raspikiln.kiln.config.sensors.SensorConfig
import org.raspikiln.kiln.sensors.Sensor
import org.raspikiln.rpi.components.MAX31855
import org.raspikiln.rpi.core.PiContext
import org.raspikiln.rpi.core.spi

private val logger = KotlinLogging.logger { }

class RpiSensorProvider(private val pi4J: PiContext) {

    fun create(sensor: SensorConfig): Sensor =
        when (val chipset = sensor.chipset) {
            is SensorChipsetConfig.MAX31855 -> MAX31855(
                sensor.name, chipset.spi.bind(sensor.name), provides = sensor.requireProvidesAs()
            ).print()
            else -> error("Unrecognized sensor type ${sensor.chipset}")
        }

    private fun SensorProtocolConfig.Spi.bind(name: String) =
        pi4J.spi {
            id(name)
            address(requireNotNull(address) { "$name address was not defined." })
            baud(baud ?: Spi.DEFAULT_BAUD)
        }

    private fun MAX31855.print() = apply {
        logger.info { "Bound MAX31855 [${name()}]" }
    }
}