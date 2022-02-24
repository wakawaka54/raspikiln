package org.raspikiln.rpi

import com.pi4j.io.spi.Spi
import mu.KotlinLogging
import org.raspikiln.kiln.config.MAX31855Config
import org.raspikiln.kiln.config.ProtocolConfig
import org.raspikiln.kiln.config.SensorConfig
import org.raspikiln.kiln.kilnProviderError
import org.raspikiln.kiln.sensors.Sensor
import org.raspikiln.rpi.components.MAX31855
import org.raspikiln.rpi.core.PiContext
import org.raspikiln.rpi.core.spi

private val logger = KotlinLogging.logger { }

class RpiSensorProvider(private val pi4J: PiContext) {
    fun create(sensorConfig: SensorConfig): Sensor =
        when (sensorConfig) {
            is SensorConfig.TemperatureSensorConfig -> sensorConfig.create()
            else -> kilnProviderError(message = "Unknown sensor config type ${sensorConfig::class}")
        }

    private fun SensorConfig.TemperatureSensorConfig.create(): Sensor =
        when (val typedSensor = sensor) {
            is MAX31855Config -> MAX31855(name, location, zone, typedSensor.spi.bind(name)).print()
            else -> kilnProviderError(message = "Unknown specific sensor interface $name ${typedSensor::class}")
        }

    private fun ProtocolConfig.Spi.bind(name: String) =
        pi4J.spi {
            id(name)
            address(requireNotNull(address) { "$name address was not defined." })
            baud(baud ?: Spi.DEFAULT_BAUD)
        }

    private fun MAX31855.print() = apply {
        logger.info { "Bound MAX31855 [${name()}] located [${location().name()}] zone [${zone()}]." }
    }
}