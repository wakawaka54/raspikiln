package org.raspikiln.embedded.timeseries.file

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.single
import org.raspikiln.core.Time
import org.raspikiln.embedded.timeseries.TimeSeriesEntry
import org.raspikiln.embedded.timeseries.TimeSeriesRecord
import java.io.File
import java.time.Duration
import java.time.ZonedDateTime
import kotlin.random.Random

class FileTimeSeriesDBTest : FunSpec({
    val directory = tempdir()
    val random = Random(0)

    val timeSeriesDb = FileTimeSeriesDB(FileTimeSeriesDB.Options(
        directory = directory.absolutePath
    ))

    context("collection") {
        val collection = timeSeriesDb.openCollection("metrics", rotateFrequency = Duration.ofSeconds(1))

        test("write / read metrics") {
            val startTime = Time.nowZ()
            val valueArb = Arb.double(60.0..100.0)

            repeat(1_000) {
                collection.write(
                    TimeSeriesEntry(
                        name = "metric",
                        value = valueArb.next()
                    )
                )
            }

            val values = collection.read()
                .onEach { record ->
                    record.timestamp shouldBeGreaterThan startTime
                    record.name shouldBe "metric"
                    record.value shouldBeGreaterThanOrEqual 60.0
                    record.value shouldBeLessThanOrEqual 100.0
                }
                .toList()

            values.count() shouldBe 1_000
            values.map { it.timestamp } shouldBeSortedWith { left, right -> -left.compareTo(right) }
        }
    }
})