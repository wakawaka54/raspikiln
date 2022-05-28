package org.raspikiln.kiln.legacysensors

import kotlinx.coroutines.delay
import org.raspikiln.core.units.average
import org.raspikiln.kiln.common.CoroutineRuntime
import org.raspikiln.kiln.common.DispatcherScope
import java.util.concurrent.ArrayBlockingQueue
import kotlin.time.Duration

/**
 * Read a temperature sensor with sampling.
 */
fun TemperatureSensor.withSampling(windowDuration: Duration, windowSamples: Int): TemperatureSensor =
    SamplingTemperatureSensor(sensor = this, config = SamplingConfig(windowSamples, windowDuration))

/**
 * Temperature sensor which samples
 */
class SamplingTemperatureSensor(
    private val sensor: TemperatureSensor,
    private val config: SamplingConfig,
    coroutineRuntime: CoroutineRuntime = DispatcherScope.ApplicationIo
): TemperatureSensor by sensor {
    private val samples = ArrayBlockingQueue<TemperatureMeasurement>(config.windowSamples + 2)

    init {
        coroutineRuntime.launch { sampleLoop() }
    }

    override fun read(): TemperatureMeasurement =
        if (samples.isNotEmpty()) {
            TemperatureMeasurement(temperature = samples.map { it.temperature }.average())
        } else {
            sensor.read()
        }

    private suspend fun sampleLoop() {
        val sampleSleepTime = config.windowDuration / config.windowSamples

        while (true) {
            while (samples.remainingCapacity() < 2) {
                samples.remove()
            }

            samples.add(sensor.read())
            delay(sampleSleepTime)
        }
    }
}

data class SamplingConfig(

    /**
     * Samples to collect per window.
     */
    val windowSamples: Int,

    /**
     * Window duration.
     */
    val windowDuration: Duration
)