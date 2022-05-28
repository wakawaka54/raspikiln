package org.raspikiln.kiln.sensors.sampling

import com.google.common.util.concurrent.AbstractScheduledService
import org.raspikiln.core.services.AbstractFixedScheduledService
import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.sensors.TemperatureMeasurement
import org.raspikiln.kiln.sensors.TemperatureSensor
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class SamplingTemperatureSensor(
    private val name: String,
    private val sensor: TemperatureSensor,
    private val window: Duration,
    private val samples: Int
) : TemperatureSensor, AbstractScheduledService() {

    private val sampleMap = ConcurrentHashMap<KilnLocation, SlidingWindowSampler<TemperatureMeasurement>>()

    override fun name(): String = name

    override fun temperature(): List<TemperatureMeasurement> =
        sampleMap.map { (location, sampler) -> sampler.read(location) }

    override fun scheduler(): Scheduler = Scheduler.newFixedRateSchedule(
        0.seconds.toJavaDuration(), window.toJavaDuration()
    )

    override fun runOneIteration() {
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