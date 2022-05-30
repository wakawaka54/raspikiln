package org.raspikiln.core.services

import com.google.common.util.concurrent.AbstractScheduledService
import com.google.common.util.concurrent.Service
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private val logger = KotlinLogging.logger {  }

/**
 * Creates a scheduled service.
 */
fun scheduledService(name: String, delay: Duration = 0.seconds, period: Duration, block: suspend () -> Unit): Service =
    ScheduledServiceSpec(name, delay, period, block)

/**
 * Scheduled service.
 */
abstract class ScheduledService(
    private val name: String,
    private val delay: Duration,
    private val period: Duration
) : AbstractScheduledService() {

    override fun serviceName(): String = name

    override fun scheduler(): Scheduler = Scheduler.newFixedRateSchedule(delay.toJavaDuration(), period.toJavaDuration())

    override fun startUp() {
        logger.info { "Starting service [$name]" }
    }

    override fun runOneIteration() = runBlocking { run() }

    override fun shutDown() {
        logger.info { "Stopping service [$name]" }
    }

    protected abstract suspend fun run()
}

private class ScheduledServiceSpec(
    name: String,
    delay: Duration,
    period: Duration,
    private val fn: suspend () -> Unit
) : ScheduledService(name, delay, period) {
    override suspend fun run() = fn()
}