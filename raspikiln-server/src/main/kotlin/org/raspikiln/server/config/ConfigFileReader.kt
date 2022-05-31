package org.raspikiln.server.config

import com.fasterxml.jackson.module.kotlin.readValue
import org.raspikiln.core.YamlObjectMapper
import org.raspikiln.kiln.config.Config
import java.io.File

/**
 * Reads a config file.
 */
class ConfigFileReader(private val mapper: YamlObjectMapper) {
    fun read(configFile: File): Config = mapper.readValue(configFile)
}