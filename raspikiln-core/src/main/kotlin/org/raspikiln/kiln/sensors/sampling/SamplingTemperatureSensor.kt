package org.raspikiln.kiln.sensors.sampling

import org.raspikiln.core.services.scheduledService
import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.kiln.sensors.TemperatureMeasurement
import org.raspikiln.kiln.sensors.TemperatureSensor
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

class SamplingTemperatureSensor(
    private val name: String,
    private val sensor: TemperatureSensor,
    private val window: Duration,
    private val samples: Int
) : TemperatureSensor {

    private val sampleMap = ConcurrentHashMap<String, SlidingWindowSampler<TemperatureMeasurement>>()
    private val service = scheduledService(name = "$name-sampling", period = window / samples) { sample() }

    override fun name(): String = name

    override fun initialize(initializer: KilnInitializationBuilder) { initializer.registerService(service) }

    override fun temperature(): List<TemperatureMeasurement> =
        sampleMap.map { (metric, sampler) -> sampler.read(metric) }

    private fun sample() {
        sensor.temperature().forEach {
            windowSampler(it.metric).sample(it)
        }
    }

    private fun windowSampler(metric: String) = sampleMap.computeIfAbsent(metric) {
        SlidingWindowSampler(window = window, capacity = samples)
    }

    private fun SlidingWindowSampler<TemperatureMeasurement>.read(metric: String): TemperatureMeasurement {
        val samples = read()
        val sum = samples.fold(TemperatureMeasurement(metric, temperature = Temperature.Celsius.Zero)) {
                last, next -> last + next
        }

        return sum / samples.size
    }
}