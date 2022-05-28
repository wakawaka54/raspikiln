package org.raspikiln.kiln.sensors

import org.raspikiln.kiln.config.sensors.SensorConfig

/**
 * Decorates sensors with additional features.
 */
interface SensorDecoratorFactory {
    fun decorate(sensor: Sensor, config: SensorConfig): List<Sensor>
}