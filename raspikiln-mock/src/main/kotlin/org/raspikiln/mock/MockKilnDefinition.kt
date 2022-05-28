package org.raspikiln.mock

import org.raspikiln.jobs.onGlobal
import org.raspikiln.kiln.KilnBridge
import org.raspikiln.kiln.controller.PidController
import org.raspikiln.kiln.controller.TemperatureController
import org.raspikiln.kiln.controller.algorithm.TupleTerms
import org.raspikiln.kiln.legacysensors.Sensor
import org.raspikiln.kiln.legacysensors.withSampling
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.zones.KilnZoneName
import org.raspikiln.kiln.zones.KilnZoneNames
import kotlin.time.Duration.Companion.seconds

/**
 * Kiln definition that is mocked.
 */
class MockKilnDefinition : KilnBridge {
    private val mockState = MockKilnState(
        options = MockKilnState.Options(
            temperatureFormula = MockTemperatureFormula(MockTemperatureFormula.Parameters())
        )
    )

    private val temperatureSensor =
        MockTemperatureSensor(mockState).withSampling(windowDuration = 2.seconds, windowSamples = 20)

    private val heaterSwitch = MockHeaterSwitch(mockState)
    private val temperatureController = PidController(
        zones = setOf(KilnZoneNames.Zone0),
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
        MockFormulaUpdateJob(mockState).onGlobal()
    }

    override fun zones(): Set<KilnZoneName> =
        (sensors().map { it.zone() } + switches().flatMap { it.zones() }).toSet()

    override fun sensors(): List<Sensor> = listOf(temperatureSensor)

    override fun switches(): List<Switch> = listOf(heaterSwitch)

    override fun controllers(): List<TemperatureController> = listOf(temperatureController)

    override fun shutdown() {

    }
}