package org.raspikiln.kiln.sensors

interface TemperatureSensor : Sensor {
    fun temperature(): List<TemperatureMeasurement>
}