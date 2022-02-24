package org.raspikiln.core.utils

import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.toJavaDuration

fun ZonedDateTime.truncatedTo(duration: Duration): ZonedDateTime =
    ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(toEpochSecond() - (toEpochSecond() % duration.toSeconds())),
        ZoneOffset.UTC
    )

fun ZonedDateTime.toInstantString(): String = DateTimeFormatter.ISO_INSTANT.format(this)

operator fun ZonedDateTime.plus(kotlinDuration: kotlin.time.Duration): ZonedDateTime = this + kotlinDuration.toJavaDuration()