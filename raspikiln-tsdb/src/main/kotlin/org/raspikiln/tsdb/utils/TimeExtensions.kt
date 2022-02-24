package org.raspikiln.tsdb.utils

import java.time.Instant
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

typealias TimestampRange = ClosedRange<Instant>

operator fun Instant.rangeTo(instantRef: AtomicReference<Instant>) = this..instantRef.get()

operator fun Instant.plus(duration: Duration): Instant = plusMillis(duration.inWholeMilliseconds)
operator fun Instant.minus(another: Instant): Duration = (toEpochMilli() - another.toEpochMilli()).milliseconds
operator fun Instant.minus(anotherRef: AtomicReference<Instant>) = this - anotherRef.get()
operator fun Instant.minus(duration: Duration): Instant = minusMillis(duration.inWholeMilliseconds)

fun TimestampRange.intersects(another: TimestampRange) =
    (contains(another.start) or contains(another.endInclusive)) or
            (another.contains(start) or another.contains(endInclusive))

fun TimestampRange.duration() = (endInclusive.toEpochMilli() - start.toEpochMilli()).milliseconds

fun TimestampRange.coerce(other: TimestampRange) =
    (start.coerceAtLeast(other.start))..(endInclusive.coerceAtMost(other.endInclusive))