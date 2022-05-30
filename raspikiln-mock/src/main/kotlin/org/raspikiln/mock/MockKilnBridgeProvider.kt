package org.raspikiln.mock

import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.bridge.KilnBridgeProvider
import org.raspikiln.kiln.config.KilnConfig
import org.raspikiln.kiln.config.sensors.SensorConfig
import org.raspikiln.kiln.sensors.Sensor

class MockKilnBridgeProvider : KilnBridgeProvider {
    private val mockState = MockKilnState(
        options = MockKilnState.Options(
            temperatureFormula = MockTemperatureFormula(MockTemperatureFormula.Parameters())
        )
    )

    override fun name(): String = "mock"
    override fun create(config: KilnConfig): KilnBridge = MockKilnBridge(
        mockState = mockState,
        sensors = config.sensors.map { it.createSensor() }
    )

    private fun SensorConfig.createSensor(): Sensor =
        when (type) {
            MockTemperatureSensor.TYPE -> MockTemperatureSensor(
                kilnState = mockState,
                provides = requireProvidesAs(),
                options = MockTemperatureSensor.Options(noiseAmount = 1.0)
            )
            else -> error("Could not create sensor of type [$type]")
        }
}