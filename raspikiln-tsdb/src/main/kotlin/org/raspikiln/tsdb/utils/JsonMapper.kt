package org.raspikiln.tsdb.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import kotlin.reflect.KClass

object JsonMapper {
    private val mapper =ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())

    fun <T : Any> readValue(file: File, type: KClass<T>): T = mapper.readValue(file, type.java)

    fun writeValue(file: File, obj: Any) = mapper.writeValue(file, obj)
}