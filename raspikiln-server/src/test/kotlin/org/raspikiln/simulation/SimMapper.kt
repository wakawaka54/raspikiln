package org.raspikiln.simulation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.raspikiln.core.registerCoreModule

object SimMapper {
    val Json =
        ObjectMapper()
            .registerModule(JavaTimeModule())
            .registerCoreModule()
            .registerKotlinModule()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    val Yaml =
        ObjectMapper(YAMLFactory())
            .registerModule(JavaTimeModule())
            .registerCoreModule()
            .registerKotlinModule()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
}