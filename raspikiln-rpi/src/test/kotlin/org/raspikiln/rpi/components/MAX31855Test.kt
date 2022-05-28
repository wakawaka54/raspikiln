package org.raspikiln.rpi.components

import at.favre.lib.bytes.Bytes
import com.pi4j.io.spi.Spi
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.raspikiln.kiln.common.KilnLocation

class MAX31855Test : FunSpec({
    listOf(
        row("0110 0100 0000 00", 1600.00),
        row("0011 1110 1000 00", 1000.00),
        row("0000 0110 0100 11", 100.75),
        row("0000 0001 1001 00 ", 25.00),
        row( "0000 0000 0000 00", 0.00),
        row("1111 1111 1111 11", -0.25),
        row("1111 1111 1111 00", -1.00),
        row("1111 0000 0110 00", -250.0)
    ).forEach { (byteString, temperature) ->
        test("correct temperature for [$byteString] [$temperature]") {
            val measurement = MAX31855(
                name = "max",
                location = KilnLocation.Oven,
                zone = "zone",
                spi = mockSpi(byteString.bytes(padRight = 18))
            ).read()

            measurement.temperature.celsius().value shouldBe temperature
        }
    }

    listOf(
        row("0111 1111 0000", 127.0000)
    ).forEach { (byteString, junctionTemperature) ->
        test("correct junction temperature for [$byteString] [$junctionTemperature]") {
            val measurement = MAX31855(
                name = "max",
                location = KilnLocation.Oven,
                zone = "zone",
                spi = mockSpi(byteString.bytes(padLeft = 16, padRight = 4))
            ).read()

            measurement.temperature.celsius().value shouldBe junctionTemperature
        }
    }
})

private fun mockSpi(bytes: Bytes) =
    mockk<Spi> {
        every { readNBytes(4) } returns bytes.array()
    }

private fun String.bytes(padLeft: Int = 0, padRight: Int = 0): Bytes {
    val prefix = "0".repeat(padLeft)
    val suffix = "0".repeat(padRight)

    return Bytes.from(
        (prefix + this + suffix)
            .replace(" ", "")
            .chunked(8)
            .map { it.toInt(radix = 2).toByte() }
            .toByteArray()
    )
}