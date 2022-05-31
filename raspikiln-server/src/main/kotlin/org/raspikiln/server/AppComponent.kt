package org.raspikiln.server

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.raspikiln.server.config.ConfigFileReader
import kotlin.reflect.KClass

interface AppComponent {
    companion object {
        fun create(modules: List<Module>): AppComponent = KoinAppComponent(modules)
    }

    fun <T : Any> get(type: KClass<T>): T

    fun configFileReader(): ConfigFileReader = get(ConfigFileReader::class)
    fun kilnApplication(): KilnApplication = get(KilnApplication::class)
}

private class KoinAppComponent(modules: List<Module>) : AppComponent {
    private val koin = startKoin { modules(modules) }.koin

    override fun <T : Any> get(type: KClass<T>): T = koin.get(type)
}