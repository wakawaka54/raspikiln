package org.raspikiln.tsdb

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeMonotonicallyIncreasingWith
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import java.io.File
import java.time.Instant
import java.util.Comparator
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

class TimeSeriesDBTest : FunSpec({
    test("able to write and do things") {
        val db = timeSeriesDb(retention = 10.hours)
        val temperatureA = MetricGenerator(MetricIdentifier(name = "temperatureA"), 0..20)
        val temperatureB = MetricGenerator(MetricIdentifier(name = "temperatureB"), 40..60)
        var metricCount = 0

        repeat(2_000) { _ ->
            val metricsAdded = (1..10).random()
            metricCount += metricsAdded

            repeat(metricsAdded) {
                db.write(listOf(temperatureA.next(), temperatureB.next()))
            }

            listOf(temperatureA, temperatureB).forEach { metric ->
                val query = db.query(metric.identifier, Instant.now().minusSeconds(3_000)..Instant.now()).toList()
                query.forEach { metric shouldContain it.value }
                query.shouldBeMonotonicallyIncreasingWith(Comparator.comparing { it.timestamp })
                query.count() shouldBe metricCount
            }

            delay(5)
        }
    }

    test("clear partitions after retention") {
        val db = timeSeriesDb(retention = 10.seconds)
        val metricMeasurement = MetricGenerator(MetricIdentifier(name = "temperatureA"), 0..20)
        repeat(500) {
            db.write(metricMeasurement.next())
        }

        delay(12_000)

        db.write(metricMeasurement.next())

        db.query(
            metricName = metricMeasurement.identifier,
            timestampRange = Instant.now().minusSeconds(20)..Instant.now()
        ).count() shouldBeExactly 1
    }
})

data class MetricGenerator(
    val identifier: MetricIdentifier,
    val range: IntRange
) {
    infix fun shouldContain(value: Double) {
        range.first.toDouble() shouldBeLessThan value
        range.last.toDouble() shouldBeGreaterThanOrEqual value
    }

    fun next(): Measurement =
        Measurement(
            metric = identifier,
            datapoint = Datapoint(
                timestamp = Instant.now(),
                value = nextValue()
            )
        )

    private fun nextValue() = Random.Default.nextDouble(range.first.toDouble(), range.last.toDouble())
}

private fun timeSeriesDb(retention: Duration = 10.hours) =
    StandardTimeSeriesDB(options = StandardTimeSeriesDB.Options(
        directory = File("data-test"),
        partitionDuration = 10.seconds,
        partitionCapacity = 1_000,
        retention = retention,
        deleteDelayTime = 0.seconds
    )).apply { purgeAll() }