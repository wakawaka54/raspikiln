package org.raspikiln.core.jackson

import com.fasterxml.jackson.databind.module.SimpleModule
import org.raspikiln.core.units.Temperature

class CoreJacksonModule : SimpleModule() {
    init {
        addSerializer(Temperature::class.java, TemperatureSerializer())
        addDeserializer(Temperature::class.java, TemperatureDeserializer())
    }
}