package org.raspikiln.jobs

import java.time.ZonedDateTime

/**
 * Job which is scheduled.
 */
interface ScheduledJob {

    /**
     * Name of the scheduled job.
     */
    val name: String

    fun first(): ZonedDateTime?

    /**
     * The next time the job should execute.
     */
    fun next(last: ZonedDateTime): ZonedDateTime?

    /**
     * Run the scheduled job.
     */
    fun run()
}