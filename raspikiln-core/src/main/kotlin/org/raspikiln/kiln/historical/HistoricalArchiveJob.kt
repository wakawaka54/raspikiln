package org.raspikiln.kiln.historical

import org.raspikiln.core.utils.plus
import org.raspikiln.jobs.ScheduledJob
import org.raspikiln.kiln.Kiln
import java.time.ZonedDateTime
import kotlin.time.Duration

class HistoricalArchiveJob(
    private val kiln: Kiln,
    private val options: Options
) : ScheduledJob {
    override val name: String = "KilnHistoricalArchive"

    override fun first(): ZonedDateTime? = ZonedDateTime.now()

    override fun next(last: ZonedDateTime): ZonedDateTime = last + options.interval

    override fun run() {
        kiln.historical().record(state = kiln.state())
    }

    data class Options(
        val interval: Duration
    )
}