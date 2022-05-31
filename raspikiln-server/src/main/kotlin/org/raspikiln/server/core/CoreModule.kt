package org.raspikiln.server.core

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.koin.dsl.module
import org.raspikiln.core.YamlObjectMapper
import org.raspikiln.core.registerCoreModule

val CoreModule = module {
    single {
        YamlObjectMapper().apply {
            registerModules(JavaTimeModule())
                .registerCoreModule()
                .registerKotlinModule()
                .registerModules(getAll())
        }
    }
} + DatabaseModule