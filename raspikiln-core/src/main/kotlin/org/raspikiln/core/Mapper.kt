package org.raspikiln.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.raspikiln.core.jackson.CoreJacksonModule
import org.raspikiln.kiln.jackson.KilnConfigModule

object Mapper {
    private val jsonMapper = ObjectMapper()
        .registerKotlinModule()
        .registerCoreModule()

    private val yamlMapper = ObjectMapper(YAMLFactory())
        .registerKotlinModule()
        .registerCoreModule()

    fun jsonMapper() = jsonMapper

    fun yamlMapper() = yamlMapper
}

fun ObjectMapper.registerCoreModule(): ObjectMapper = registerModules(CoreJacksonModule(), KilnConfigModule())