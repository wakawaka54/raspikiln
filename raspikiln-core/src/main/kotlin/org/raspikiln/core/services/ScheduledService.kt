package org.raspikiln.core.services

import com.google.common.util.concurrent.AbstractScheduledService
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private val logger = KotlinLogging.logger {  }

/**
 * Creates a scheduled service.
 */
fun scheduledService(name: String, delay: Duration = 0.seconds, period: Duration, block: suspend () -> Unit) =
    ScheduledService(name, delay, period, block)

/**
 * Scheduled service.
 */
class ScheduledService(
    private val name: String,
    private val delay: Duration,
    private val period: Duration,
    private val fn: suspend () -> Unit
) : AbstractScheduledService() {

    override fun serviceName(): String = name

    override fun scheduler(): Scheduler = Scheduler.newFixedRateSchedule(delay.toJavaDuration(), period.toJavaDuration())

    override fun startUp() {
        logger.info { "Starting service [$name]" }
    }

    override fun runOneIteration() = runBlocking { fn() }

    override fun shutDown() {
        logger.info { "Stopping service [$name]" }
    }
}