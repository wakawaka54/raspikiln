package org.raspikiln.mock

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.config.sensors.SensorMeasurementConfig
import org.raspikiln.kiln.sensors.TemperatureMeasurement
import org.raspikiln.kiln.sensors.TemperatureSensor

class MockTemperatureSensor(
    private val kilnState: MockKilnState,
    private val provides: List<SensorMeasurementConfig.Temperature>,
    private val options: Options = Options(),
) : TemperatureSensor {
    companion object {
        const val TYPE = "mock-temperature"
        private const val THERMOCOUPLE_ADDRESS = "thermocouple"
    }

    override fun name(): String = "mock-temperature"

    override fun temperature(): List<TemperatureMeasurement> =
        listOfNotNull(
            maybeMeasurement(THERMOCOUPLE_ADDRESS) { kilnState.temperature().withNoise(options.noiseAmount) }
        )

    private fun maybeMeasurement(address: String, temperatureFn: () -> Temperature): TemperatureMeasurement? {
        val providesConfig = provides.find { it.address == address } ?: return null
        return TemperatureMeasurement(metric = providesConfig.metric, temperature = temperatureFn())
    }

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