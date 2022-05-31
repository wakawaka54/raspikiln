package org.raspikiln.core.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
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
                unit = Temperature.Celsius.LongName,
                value = value.celsius().value
            )
        )
    }
}

class TemperatureDeserializer : StdDeserializer<Temperature>(Temperature::class.java) {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): Temperature =
        when (parser.currentToken()) {
            JsonToken.START_OBJECT -> parser.readTemperatureAmount()
            JsonToken.VALUE_STRING -> parser.readTemperatureString()
            else -> error("Unknown temperature format")
        }

    private fun JsonParser.readTemperatureString(): Temperature {
        val temperatureString = readValueAs(String::class.java).lowercase()
        return when {
            temperatureString.endsWith(Temperature.Celsius.ShortName.lowercase()) -> Temperature.Celsius(
                value = temperatureString.removeSuffix(Temperature.Celsius.ShortName.lowercase()).toDouble()
            )
            temperatureString.endsWith(Temperature.Fahrenheit.ShortName.lowercase()) -> Temperature.Fahrenheit(
                value = temperatureString.removeSuffix(Temperature.Fahrenheit.ShortName.lowercase()).toDouble()
            )
            else -> error("Unknown temperature string missing [C or F] [$temperatureString]")
        }
    }

    private fun JsonParser.readTemperatureAmount(): Temperature {
        val temperatureAmount = readValueAs(TemperatureAmount::class.java)
        return when (temperatureAmount.unit.lowercase()) {
            Temperature.Celsius.LongName.lowercase() -> Temperature.Celsius(value = temperatureAmount.value)
            Temperature.Fahrenheit.LongName.lowercase() -> Temperature.Fahrenheit(value = temperatureAmount.value)
            else -> error("Unknown temperature unit ${temperatureAmount.unit}")
        }
    }
}

private data class TemperatureAmount(
    val unit: String,
    val value: Double
)