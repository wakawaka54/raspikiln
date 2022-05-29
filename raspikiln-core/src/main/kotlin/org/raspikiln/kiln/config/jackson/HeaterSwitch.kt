package org.raspikiln.kiln.config.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.raspikiln.kiln.switches.SwitchState

class SwitchStateSerializer : StdSerializer<SwitchState>(SwitchState::class.java) {
    override fun serialize(value: SwitchState, gen: JsonGenerator, provider: SerializerProvider) {
        when (value) {
            SwitchState.On -> gen.writeString("on")
            SwitchState.Off -> gen.writeString("off")
        }
    }
}

class SwitchStateDeserializer : StdDeserializer<SwitchState>(SwitchState::class.java) {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): SwitchState =
        when (val text = parser.valueAsString?.lowercase()) {
            "on" -> SwitchState.On
            "off" -> SwitchState.Off
            else -> error("Unknown switch state [$text]")
        }
}