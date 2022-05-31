package org.raspikiln.kiln.state

import org.raspikiln.core.services.scheduledService
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.kiln.initialization.KilnInitializer
import org.raspikiln.kiln.metrics.KilnMetricsRegistry
import org.raspikiln.kiln.sensors.TemperatureSensor
import org.raspikiln.kiln.switches.Switch
import kotlin.time.Duration.Companion.seconds

class KilnStateService(
    private val metrics: KilnMetricsRegistry,
    private val bridge: KilnBridge,
    private val state: KilnState
): KilnInitializer {

    private val service = scheduledService(name = "KilnStateService", period = 2.seconds) { update() }

    override fun initialize(builder: KilnInitializationBuilder) {
        builder.registerService(service)
    }

    private fun update() {
        bridge.sensors().forEach { sensor ->
            when (sensor) {
                is TemperatureSensor -> sensor.readState()
            }
        }

        bridge.switches().forEach { switch -> switch.readState() }
    }

    private fun TemperatureSensor.readState() {
        temperature().forEach { measurement ->
            metrics.gauge(measurement.metric, measurement.temperature.celsius().value)
            state.update(measurement.metric, measurement.temperature)
        }
    }

    private fun Switch.readState() {
        measurements().forEach { measurement ->
            metrics.gauge(measurement.metric, measurement.state.numeric().toDouble())
            state.update(measurement.metric, measurement.state)
        }
    }
}