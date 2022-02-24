package org.raspikiln.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object YamlMapper {
    private val mapper =
        ObjectMapper(YAMLFactory())
            .registerKotlinModule()

    fun mapper() = mapper
}