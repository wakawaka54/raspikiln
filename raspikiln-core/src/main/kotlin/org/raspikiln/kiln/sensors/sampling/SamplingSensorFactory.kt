package org.raspikiln.kiln.sensors.sampling

import org.raspikiln.kiln.config.sensors.SensorConfig
import org.raspikiln.kiln.config.sensors.SensorSamplingConfig
import org.raspikiln.kiln.sensors.Sensor
import org.raspikiln.kiln.sensors.SensorDecoratorFactory
import org.raspikiln.kiln.sensors.TemperatureSensor
import kotlin.time.toKotlinDuration

class SamplingSensorFactory : SensorDecoratorFactory {
    override fun decorate(sensor: Sensor, config: SensorConfig): List<Sensor> {
        val sampling = config.sampling ?: return listOf(sensor)
        return when (sensor) {
            is TemperatureSensor -> listOf(sensor.withSampling(sampling))
            else -> error("Unknown sensor type with sampling [${sensor::class}]")
        }
    }

    private fun TemperatureSensor.withSampling(sampling: SensorSamplingConfig) =
        SamplingTemperatureSensor(
            name = "sampling-${name()}",
            sensor = this,
            window = sampling.window.toKotlinDuration(),
            samples = sampling.samples
        )
}