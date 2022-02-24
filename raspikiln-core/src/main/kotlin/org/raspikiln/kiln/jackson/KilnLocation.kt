package org.raspikiln.kiln.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import org.raspikiln.kiln.common.KilnLocation

private object KilnLocations {
    const val Oven = "oven"
}

class KilnLocationDeserializer : StdDeserializer<KilnLocation>(KilnLocation::class.java) {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): KilnLocation =
        when (val node = parser.readValueAsTree<JsonNode>()) {
            is TextNode -> node.kilnLocation()
            else -> error("Cannot parse kiln location [$node]")
        }

    private fun TextNode.kilnLocation(): KilnLocation =
        when(textValue().lowercase()) {
            KilnLocations.Oven -> KilnLocation.Oven
            else -> error("Unrecognized kiln location ${textValue()}")
        }
}