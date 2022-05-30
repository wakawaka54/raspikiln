package org.raspikiln.kiln.initialization

class KilnInitializationFactory {
    fun create(vararg initializers: KilnInitializer): KilnInitialization {
        val builder = KilnInitializationBuilder()

        initializers.forEach { it.initialize(builder) }

        return builder.build()
    }
}