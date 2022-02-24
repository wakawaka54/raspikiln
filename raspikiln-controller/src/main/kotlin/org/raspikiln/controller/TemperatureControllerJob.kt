package org.raspikiln.controller

import mu.KotlinLogging
import org.raspikiln.core.utils.plus
import org.raspikiln.jobs.ScheduledJob
import org.raspikiln.jobs.onGlobal
import org.raspikiln.kiln.Kiln
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger { }

class TemperatureControllerJob(
    private val kiln: Kiln,
    private val temperatureController: KilnTemperatureController
) : ScheduledJob {
    init {
        logger.info { "Starting Kiln Temperature Controller..." }
    }

    override val name: String = "TemperatureController"

    override fun first(): ZonedDateTime? = ZonedDateTime.now()

    override fun next(last: ZonedDateTime): ZonedDateTime = last + 3.seconds

    override fun run() {
        kiln.zones().forEach { temperatureController.evaluate(it) }
    }
}