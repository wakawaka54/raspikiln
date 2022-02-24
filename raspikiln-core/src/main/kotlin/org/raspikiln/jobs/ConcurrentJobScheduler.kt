package org.raspikiln.jobs

import mu.KotlinLogging
import org.apache.commons.lang3.concurrent.BasicThreadFactory
import org.raspikiln.core.utils.toInstantString
import java.time.Duration
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicBoolean

private val logger = KotlinLogging.logger {  }

class ConcurrentJobScheduler(private val options: Options) : JobScheduler {
    private val scheduleQueue = PriorityBlockingQueue<ScheduledEntry>(10, Comparator.comparing { it.time })

    private val running = AtomicBoolean(false)
    private val monitor = Executors.newSingleThreadExecutor(
        BasicThreadFactory.Builder().namingPattern("job-scheduler-monitor-%d").build()
    )
    private val jobQueue = LinkedBlockingQueue<Runnable>(options.capacity)
    private val executors = ThreadPoolExecutor(
        options.threads, options.threads, 0L, TimeUnit.MILLISECONDS,
        jobQueue, BasicThreadFactory.Builder().namingPattern("job-executor-%d").build()
    )

    init {
        Runtime.getRuntime().addShutdownHook(Thread { stop() })
    }

    override fun start() {
        if (!running.getAndSet(true)) {
            logger.info { "Starting JobScheduler..." }
            monitor.submit { monitor() }
        }
    }

    override fun stop() {
        logger.info { "Stopping JobScheduler with [${jobQueue.size}] pending jobs." }

        running.set(false)
        monitor.awaitTermination(jobQueue.size * 100L , TimeUnit.MILLISECONDS)
        executors.shutdown()
    }

    override fun schedule(job: ScheduledJob) { job.schedule(last = null) }

    private fun monitor() {
        while (running.get()) {
            try {
                scheduleQueue.runJobs()
            } catch (ex: InterruptedException) {
                throw ex
            } catch (ex: Exception) {
                logger.warn(ex) { "Unhandled exception in job monitor thread." }
            }

            Thread.sleep(options.precision.toMillis())
        }
    }

    private fun Queue<ScheduledEntry>.runJobs() {
        val now = ZonedDateTime.now()
        while (isNotEmpty() && peek().time <= now) {
            val head = remove()
            executors.submit { head.execute() }
        }
    }

    private fun ScheduledEntry.execute() {
        try {
            logger.debug { "Running scheduled job [${job.name}]." }
            job.schedule(last = time)
            job.run()
        } catch (ex: Throwable) {
            logger.error(ex) { "Scheduled job [${job.name}] failed." }
        }
    }

    private fun ScheduledJob.schedule(last: ZonedDateTime?) {
        val next = last?.let { next(it) } ?: first()
        if (next != null) {
            logger.debug { "Scheduling job [${this.name}] at [${next.toInstantString()}]" }
            scheduleQueue.add(ScheduledEntry(time = next, job = this))
        } else {
            logger.debug { "Not scheduled job [${this.name}] due to no next time." }
        }
    }

    data class Options(
        val threads: Int = 2,
        val capacity: Int = 20,
        val precision: Duration = Duration.ofSeconds(1)
    )
}

private data class ScheduledEntry(
    val time: ZonedDateTime,
    val job: ScheduledJob
)