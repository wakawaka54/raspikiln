package org.raspikiln.kiln.programs.types

import com.fasterxml.jackson.databind.JsonNode

data class ProgramDefinition(
    val name: String,
    val options: JsonNode
)