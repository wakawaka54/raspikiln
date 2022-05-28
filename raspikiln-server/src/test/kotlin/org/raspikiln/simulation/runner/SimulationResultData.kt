package org.raspikiln.simulation.runner

import java.time.Instant

data class SimulationResultData(
    val options: SimulationRunnerOptions,
    val creationTime: Instant = Instant.now(),
    val states: List<SimulationStepState>
)

data class SimulationStepState(
    val timestamp: Instant,
    val setpoint: Double,
    val temperature: Double
)