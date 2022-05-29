package org.raspikiln.server

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.raspikiln.server.core.CoreModule
import kotlin.reflect.KClass

interface AppComponent {
    companion object {
        fun create(module: Module): AppComponent = KoinAppComponent(module)
    }

    fun <T : Any> get(type: KClass<T>): T

    fun kilnApplication() = get(KilnApplication::class)
}

private class KoinAppComponent(module: Module) : AppComponent {
    private val koin = koinApplication { modules(module + CoreModule) }.koin

    override fun <T : Any> get(type: KClass<T>): T = koin.get(type)
}