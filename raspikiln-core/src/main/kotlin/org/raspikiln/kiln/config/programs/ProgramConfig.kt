package org.raspikiln.kiln.config.programs

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import org.raspikiln.core.units.Temperature

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
sealed interface ProgramConfig {
    val name: String

    @JsonTypeName("manual")
    data class Manual(
        override val name: String,
        val controllers: List<String>
    ) : ProgramConfig

    @JsonTypeName("automatic")
    data class Automatic(
        override val name: String,
        val steps: List<Step>
    ) : ProgramConfig {
        data class Step(
            val temperature: Temperature,
            val ramp: Temperature,
            val controller: String
        )
    }
}