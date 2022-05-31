package org.raspikiln.kiln.programs.types

import com.fasterxml.jackson.databind.JsonNode

data class StartProgramInput(
    val name: String,
    val options: JsonNode
)