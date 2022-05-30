package org.raspikiln.kiln.state

import org.raspikiln.core.services.scheduledService
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.kiln.initialization.KilnInitializer
import org.raspikiln.kiln.sensors.TemperatureSensor
import kotlin.time.Duration.Companion.seconds

class KilnStateService(private val bridge: KilnBridge): KilnInitializer {
    private val service = scheduledService(name = "KilnStateService", period = 2.seconds) { update() }
    private val state = KilnState()

    fun state(): KilnState = state

    override fun initialize(builder: KilnInitializationBuilder) {
        builder.registerService(service)
    }

    private fun update() {
        bridge.sensors().forEach { sensor ->
            when (sensor) {
                is TemperatureSensor -> sensor.readState()
            }
        }
    }

    private fun TemperatureSensor.readState() {
        temperature().forEach { measurement -> state.update(measurement.location, measurement.temperature) }
    }
}