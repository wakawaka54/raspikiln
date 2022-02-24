package org.raspikiln.core.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.raspikiln.core.units.Temperature


class TemperatureSerializer : StdSerializer<Temperature>(Temperature::class.java) {
    override fun serialize(
        value: Temperature,
        gen: JsonGenerator,
        provider: SerializerProvider
    ) {
        gen.writeObject(
            TemperatureAmount(
                unit = Temperature.Celsius.UnitNmae,
                value = value.celsius().value
            )
        )
    }
}

class TemperatureDeserializer : StdDeserializer<Temperature>(Temperature::class.java) {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): Temperature {
        val temperatureAmount = parser.readValueAs(TemperatureAmount::class.java)
        return when (temperatureAmount.unit) {
            Temperature.Celsius.UnitNmae -> Temperature.Celsius(value = temperatureAmount.value)
            Temperature.Fahrenheit.UnitNmae -> Temperature.Fahrenheit(value = temperatureAmount.value)
            else -> error("Unknown temperature unit ${temperatureAmount.unit}")
        }
    }
}

private data class TemperatureAmount(
    val unit: String,
    val value: Double
)