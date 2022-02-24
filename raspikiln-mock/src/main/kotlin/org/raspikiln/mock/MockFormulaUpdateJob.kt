package org.raspikiln.mock

import org.raspikiln.jobs.ScheduledJob
import java.time.ZonedDateTime

/**
 * Updates mock formula.
 */
class MockFormulaUpdateJob(private val kilnState: MockKilnState) : ScheduledJob {
    override val name: String = "MockFormulaJob"

    override fun first(): ZonedDateTime? = ZonedDateTime.now()

    override fun next(last: ZonedDateTime): ZonedDateTime = last.plusSeconds(1)

    override fun run() {
        kilnState.update()
    }
}