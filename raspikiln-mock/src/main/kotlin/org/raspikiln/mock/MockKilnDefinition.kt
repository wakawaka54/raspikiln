package org.raspikiln.mock

import org.raspikiln.jobs.onGlobal
import org.raspikiln.kiln.KilnDefinition
import org.raspikiln.kiln.sensors.Sensor
import org.raspikiln.kiln.sensors.withSampling
import org.raspikiln.kiln.switches.Switch
import org.raspikiln.kiln.zones.KilnZoneName
import kotlin.time.Duration.Companion.seconds

/**
 * Kiln definition that is mocked.
 */
class MockKilnDefinition : KilnDefinition {
    private val mockState = MockKilnState(
        options = MockKilnState.Options(
            temperatureFormula = MockTemperatureFormula(MockTemperatureFormula.Parameters())
        )
    )

    private val temperatureSensor =
        MockTemperatureSensor(mockState).withSampling(windowDuration = 2.seconds, windowSamples = 20)

    private val heaterSwitch = MockHeaterSwitch(mockState)

    init {
        MockFormulaUpdateJob(mockState).onGlobal()
    }

    override fun zones(): Set<KilnZoneName> =
        (sensors().map { it.zone() } + switches().flatMap { it.zones() }).toSet()

    override fun sensors(): List<Sensor> = listOf(temperatureSensor)

    override fun switches(): List<Switch> = listOf(heaterSwitch)

    override fun shutdown() {

    }
}