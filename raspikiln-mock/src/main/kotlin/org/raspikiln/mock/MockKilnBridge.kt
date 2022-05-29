package org.raspikiln.mock

import org.raspikiln.core.services.scheduledService
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.controller.PidController
import org.raspikiln.kiln.controller.TemperatureController
import org.raspikiln.kiln.controller.algorithm.TupleTerms
import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.kiln.sensors.Sensor
import org.raspikiln.kiln.switches.Switch
import kotlin.time.Duration.Companion.seconds

/**
 * Kiln definition that is mocked.
 */
class MockKilnBridge : KilnBridge {
    private val mockState = MockKilnState(
        options = MockKilnState.Options(
            temperatureFormula = MockTemperatureFormula(MockTemperatureFormula.Parameters())
        )
    )

    private val temperatureSensor = MockTemperatureSensor(mockState)

    private val heaterSwitch = MockHeaterSwitch(mockState, setOf(KilnLocation.Oven))

    private val temperatureController = PidController(
        locations = setOf(KilnLocation.Oven),
        options = PidController.Options(
            dutyCycle = 2.seconds,
            gainTerms = TupleTerms(
                p = 20.0,
                i = 0.0,
                d = 0.0
            )
        )
    )

    init {
        // MockFormulaUpdateJob(mockState).onGlobal()
    }

    override fun initialize(builder: KilnInitializationBuilder) {
        builder.registerService(
            scheduledService(name = "MockStateUpdate", period = 1.seconds) { mockState.update() }
        )
    }

    override fun sensors(): List<Sensor> = listOf(temperatureSensor)

    override fun switches(): List<Switch> = listOf(heaterSwitch)

    override fun controllers(): List<TemperatureController> = listOf(temperatureController)

    override fun shutdown() {

    }
}