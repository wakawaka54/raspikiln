package org.raspikiln.kiln.controller.algorithm

import kotlin.math.absoluteValue
import kotlin.time.Duration
import kotlin.time.DurationUnit.SECONDS

/**
 * PID algorithm.
 */
class ProportionalIntegralDerivative(
    /**
     * Gain terms.
     */
    private val gainTerms: TupleTerms,

    /**
     * Output range.
     */
    private val outputBounds: ClosedRange<Double>,

    /**
     * Output scaling factor.
     */
    private val outputScale: Double = 1.0
) {
    private val kp = gainTerms.p
    private val ki = gainTerms.i
    private val kd = gainTerms.d
    private var cummulativeError = 0.0
    private var previousError = 0.0

    fun reset() {

    }

    fun compute(duration: Duration, actual: Double, target: Double): Computation {
        val error = target - actual
        val timeDelta = duration.toDouble(SECONDS)

        cummulativeError += error * timeDelta

        if (error.absoluteValue > 5.0) {
            cummulativeError = 0.0
        }

        val derivative = (error - previousError) / timeDelta
        previousError = error

        val p = kp * error
        val i = ki * cummulativeError
        val d = kd * derivative
        val outputUnbounded = p + i + d
        val output = outputUnbounded.coerceIn(outputBounds) / outputScale

        return Computation(
            output = output,
            outputUnbounded = outputUnbounded,
            actual = actual,
            target = target,
            durationSeconds = timeDelta,
            error = error,
            terms = TupleTerms(p, i = i, d = d),
            gains = gainTerms
        )
    }

    data class Computation(
        val output: Double,
        val outputUnbounded: Double,
        val actual: Double,
        val target: Double,
        val durationSeconds: Double,
        val error: Double,
        val terms: TupleTerms,
        val gains: TupleTerms
    )
}

data class TupleTerms(
    val p: Double,
    val i: Double = 0.0,
    val d: Double = 0.0
)