package org.raspikiln.kiln.initialization

import org.raspikiln.kiln.metrics.KilnMetricsRegistry

class KilnInitializationFactory(private val metrics: KilnMetricsRegistry) {

    fun initialize(init: KilnInitializationBuilder.() -> Unit) =
        KilnInitializationBuilder(metrics).apply(init).build()
}

fun KilnInitializationBuilder.initialize(vararg initializers: KilnInitializer) = apply {
    initializers.forEach { it.initialize(this) }
}