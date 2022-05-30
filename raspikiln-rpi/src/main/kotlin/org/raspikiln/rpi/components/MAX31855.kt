package org.raspikiln.rpi.components

import at.favre.lib.bytes.Bytes
import com.pi4j.io.spi.Spi
import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.config.sensors.SensorMeasurementConfig
import org.raspikiln.kiln.sensors.TemperatureMeasurement
import org.raspikiln.kiln.sensors.TemperatureSensor

/**
 * Thermocouple interface IC.
 *
 * Docs https://datasheets.maximintegrated.com/en/ds/MAX31855.pdf
 */
class MAX31855(
    private val name: String,
    private val spi: Spi,
    private val provides: List<SensorMeasurementConfig.Temperature>
) : TemperatureSensor {
    companion object {
        const val TYPE = "MAX31855"

        private const val DataLengthBytes = 4
        private const val THERMOCOUPLE_ADDRESS = "thermocouple"
        private const val JUNCTION_ADDRESS = "junction"
    }

    override fun name(): String = name

    override fun temperature(): List<TemperatureMeasurement> {
        val bytes = readBytes()

        return listOfNotNull(
            maybeMeasurement(THERMOCOUPLE_ADDRESS, bytes.thermocoupleTemperature().celsius()),
            maybeMeasurement(JUNCTION_ADDRESS, bytes.junctionTemperature().celsius())
        )
    }

    private fun readBytes() = Bytes.wrap(spi.readNBytes(DataLengthBytes))

    private fun Bytes.thermocoupleTemperature() =
        toBigInteger().shiftRight(18).intValueExact().toDouble() * 0.25

    private fun Bytes.junctionTemperature() =
        and(Bytes.parseHex("0x0000FFFF"))
            .toBigInteger()
            .shiftRight(4)
            .intValueExact()
            .toDouble() * 0.0625

    private fun maybeMeasurement(address: String, temperature: Temperature): TemperatureMeasurement? {
        val providesConfig = provides.find { it.address == address } ?: return null
        return TemperatureMeasurement(metric = providesConfig.metric, temperature = temperature)
    }

    private fun Double.celsius() = Temperature.Celsius(this)
}