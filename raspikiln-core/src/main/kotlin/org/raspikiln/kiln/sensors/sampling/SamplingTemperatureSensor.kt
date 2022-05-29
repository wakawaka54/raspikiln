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

    private val sampleMap = ConcurrentHashMap<KilnLocation, SlidingWindowSampler<TemperatureMeasurement>>()
    private val service = scheduledService(name = "$name-sampling", period = window / samples) { sample() }

    override fun name(): String = name

    override fun initialize(initializer: KilnInitializationBuilder) { initializer.registerService(service) }

    override fun temperature(): List<TemperatureMeasurement> =
        sampleMap.map { (location, sampler) -> sampler.read(location) }

    private fun sample() {
        sensor.temperature().forEach {
            windowSampler(it.location).sample(it)
        }
    }

    private fun windowSampler(location: KilnLocation) = sampleMap.computeIfAbsent(location) {
        SlidingWindowSampler(window = window, capacity = samples)
    }

    private fun SlidingWindowSampler<TemperatureMeasurement>.read(location: KilnLocation): TemperatureMeasurement {
        val samples = read()
        val sum = samples.fold(TemperatureMeasurement(location, temperature = Temperature.Celsius.Zero)) {
                last, next -> last + next
        }

        return sum / samples.size
    }
}