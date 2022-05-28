package org.raspikiln.simulation.runner

import org.raspikiln.kiln.controller.algorithm.TupleTerms
import kotlin.time.Duration.Companion.minutes

data class SimulationRunnerOptions(
    val gainTerms: TupleTerms = TupleTerms(
        p = 29.2,
        i = 0.072,
        d = 640.0
    ),
    val steps: List<StepOptions> = listOf(
        StepOptions(
            duration = 30.minutes.inWholeMinutes.toInt(),
            setpoint = 40.0
        )
    )
) {
    data class StepOptions(
        val duration: Int,
        val setpoint: Double
    )
}