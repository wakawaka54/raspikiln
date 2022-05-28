package org.raspikiln.core.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.toJavaDuration

fun ZonedDateTime.toInstantString(): String = DateTimeFormatter.ISO_INSTANT.format(this)

operator fun ZonedDateTime.plus(kotlinDuration: kotlin.time.Duration): ZonedDateTime = this + kotlinDuration.toJavaDuration()