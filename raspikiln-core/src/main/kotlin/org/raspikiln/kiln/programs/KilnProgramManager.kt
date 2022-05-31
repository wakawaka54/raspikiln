package org.raspikiln.kiln.programs

import org.raspikiln.core.services.scheduledService
import org.raspikiln.kiln.config.programs.ProgramConfig
import org.raspikiln.kiln.initialization.KilnInitializationBuilder
import org.raspikiln.kiln.initialization.KilnInitializer
import org.raspikiln.kiln.programs.types.StartProgramInput
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration.Companion.seconds

class KilnProgramManager(

    /**
     * Store which stores all persisted programs.
     */
    private val store: ProgramStore,

    /**
     * Creates programs when they are started.
     */
    private val factory: ProgramFactory
): KilnInitializer {
    private val mutex = Any()
    private val service = scheduledService(name = "KilnProgramManager", period = 1.seconds) { evaluate() }
    private val program = AtomicReference<Program>(null)

    override fun initialize(builder: KilnInitializationBuilder) {
        builder.registerService(service)
    }

    fun findAll(): List<ProgramConfig> = store.readAll()

    fun start(input: StartProgramInput) {
        synchronized(mutex) {
            program.get()?.end()
            program.set(factory.create(input))
            program.get()?.start()
        }
    }

    fun stop() {
        synchronized(mutex) {
            program.get()?.end()
            program.set(null)
        }
    }

    private fun evaluate() {
        synchronized(mutex) {
            program.get()?.evaluate()
        }
    }
}