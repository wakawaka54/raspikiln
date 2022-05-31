package org.raspikiln.kiln.module

import org.raspikiln.core.time.SystemClock
import org.raspikiln.kiln.bridge.KilnBridgeProvider
import org.raspikiln.kiln.bridge.KilnBridgeProviderRegistry
import org.raspikiln.kiln.metrics.KilnMetricsRegistry
import org.raspikiln.kiln.metrics.KilnMetricsReporter

fun kilnModule(
    metrics: KilnMetricsRegistry = KilnMetricsRegistry.standard(),
    init: KilnModuleSpec.() -> Unit
): KilnModule = KilnModuleSpec(metrics).apply(init).build()

class KilnModuleSpec(private val metrics: KilnMetricsRegistry = KilnMetricsRegistry.standard()) {
    private var systemClock: SystemClock = SystemClock.systemUTC()
    private val bridgeProviderRegistry = KilnBridgeProviderRegistry()
    private val reporters: MutableList<KilnMetricsReporter> = mutableListOf()

    fun overrideClock(clock: SystemClock) = apply {
        systemClock = clock
    }

    fun reporter(reporter: KilnMetricsReporter) = apply {
        reporters.add(reporter)
    }

    fun bridgeProvider(bridgeProvider: KilnBridgeProvider) = apply {
        bridgeProviderRegistry.register(bridgeProvider)
    }

    fun bridgeProviders(bridgeProviders: List<KilnBridgeProvider>) = apply {
        bridgeProviderRegistry.registerAll(bridgeProviders)
    }

    fun build() = KilnModule(
        systemClock = systemClock,
        metrics = metrics,
        metricReporters = reporters,
        bridgeProviderRegistry = bridgeProviderRegistry
    )
}