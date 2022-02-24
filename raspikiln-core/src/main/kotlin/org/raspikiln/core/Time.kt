package org.raspikiln.core

import java.time.ZoneOffset
import java.time.ZonedDateTime

object Time {
    fun nowZ(): ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC)
}