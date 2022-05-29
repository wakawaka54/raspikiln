package org.raspikiln.kiln.programs

import org.raspikiln.kiln.Kiln
import kotlin.time.Duration

/**
 * Evaluates the current program.
 */
class ProgramEvaluatorJob(
    private val kiln: Kiln,
    private val options: ProgramEvaluatorJobOptions
)

data class ProgramEvaluatorJobOptions(
    val interval: Duration
)