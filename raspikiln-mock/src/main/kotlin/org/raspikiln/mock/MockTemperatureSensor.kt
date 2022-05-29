package org.raspikiln.mock

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.sensors.TemperatureMeasurement
import org.raspikiln.kiln.sensors.TemperatureSensor

class MockTemperatureSensor(
    private val kilnState: MockKilnState,
    private val options: Options = Options()
) : TemperatureSensor {

    override fun name(): String = "mock-temperature"

    override fun temperature(): List<TemperatureMeasurement> =
        listOf(
            TemperatureMeasurement(location = KilnLocation.Oven, temperature = kilnState.temperature().withNoise(options.noiseAmount))
        )

    private fun Temperature.withNoise(amount: Double) =
        Temperature.Celsius(
            celsius().value + Arbritrary.double(-amount, amount)
        )

    data class Options(

        /**
         * How much noise to add on top of the temperature signal.
         */
        val noiseAmount: Double = 1.0
    )
}