package org.raspikiln.rpi.components

import at.favre.lib.bytes.Bytes
import com.pi4j.io.spi.Spi
import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.legacysensors.TemperatureMeasurement
import org.raspikiln.kiln.legacysensors.TemperatureSensor
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

        val bytes = readBytes()

        val thermocoupleTemperature =
            Temperature.Celsius(bytes.thermocoupleTemperature())

        val junctionTemperature = Temperature.Celsius(bytes.junctionTemperature())

        // 0 bit sign, 1 - 11 bits temperature . (2 bits
        /*
        val temperature = Temperature.Celsius(
            bits.getMsbInt(0 until 12).toDouble() + bits.getMsbUInt(12 until 14).toDouble() * 0.25
        )
         */

        return TemperatureMeasurement(thermocoupleTemperature)
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
}