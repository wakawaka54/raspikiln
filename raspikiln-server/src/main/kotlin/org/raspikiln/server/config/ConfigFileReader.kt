package org.raspikiln.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.raspikiln.core.Mapper
import org.raspikiln.kiln.config.Config
import java.io.File

/**
 * Reads a config file.
 */
class ConfigFileReader(private val mapper: ObjectMapper = Mapper.yamlMapper()) {
    fun read(configFile: File): Config = mapper.readValue(configFile)
}