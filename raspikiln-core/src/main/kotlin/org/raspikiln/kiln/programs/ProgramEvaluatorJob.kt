package org.raspikiln.kiln.programs

import org.raspikiln.core.utils.plus
import org.raspikiln.jobs.ScheduledJob
import org.raspikiln.kiln.Kiln
import java.time.ZonedDateTime
import kotlin.time.Duration

/**
 * Evaluates the current program.
 */
class ProgramEvaluatorJob(
    private val kiln: Kiln,
    private val options: ProgramEvaluatorJobOptions
) : ScheduledJob {
    override val name: String = "KilnProgramEvaluator"

    override fun first(): ZonedDateTime? = ZonedDateTime.now()

    override fun next(last: ZonedDateTime): ZonedDateTime = last + options.interval

    override fun run() {
        kiln.currentProgram()?.evaluate()
    }
}

data class ProgramEvaluatorJobOptions(
    val interval: Duration
)