package org.raspikiln.http.kiln.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.raspikiln.core.units.Temperature

fun ObjectMapper.registerKilnApiModule() = apply {
    registerModule(KilnApiModule())
}

private class KilnApiModule : SimpleModule() {
    init {

    }
}