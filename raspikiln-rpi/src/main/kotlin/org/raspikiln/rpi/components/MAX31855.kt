package org.raspikiln.rpi.components

import com.pi4j.io.spi.Spi
import org.raspikiln.core.bytes.asBits
import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.sensors.TemperatureMeasurement
import org.raspikiln.kiln.sensors.TemperatureSensor
import org.raspikiln.kiln.zones.KilnZoneName

/**
 * Thermocouple interface IC.
 *
 * Docs https://datasheets.maximintegrated.com/en/ds/MAX31855.pdf
 */
class MAX31855(
    private val name: String,
    private val location: KilnLocation,
    private val zone: KilnZoneName,
    private val spi: Spi
) : TemperatureSensor {
    companion object {
        private const val DataLengthBytes = 4
    }

    override fun name(): String = name

    override fun location(): KilnLocation = location

    override fun zone(): KilnZoneName = zone

    override fun read(): TemperatureMeasurement {
        // TODO Check for faults and throw errors.

        val bits = readBits()
        val temperature = Temperature.Celsius(
            bits.getMsbInt(0 until 12).toDouble() + bits.getMsbUInt(12 until 14).toDouble() * 0.25
        )

        return TemperatureMeasurement(temperature)
    }

    private fun readBits() = spi.readNBytes(DataLengthBytes).asBits()
}