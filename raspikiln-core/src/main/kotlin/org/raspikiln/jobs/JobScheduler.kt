package org.raspikiln.jobs

import java.time.Duration

/**
 * Schedules jobs.
 */
interface JobScheduler {
    companion object {
        private val Default by lazy {
            ConcurrentJobScheduler(ConcurrentJobScheduler.Options(
                threads = 2,
                capacity = 20,
                precision = Duration.ofSeconds(1)
            )).apply { start() }
        }

        /**
         * Global job scheduler.
         */
        fun global() = Default
    }

    fun start()
    fun schedule(job: ScheduledJob)
    fun stop()
}

fun <T : ScheduledJob> T.onGlobal() = apply { JobScheduler.global().schedule(this) }