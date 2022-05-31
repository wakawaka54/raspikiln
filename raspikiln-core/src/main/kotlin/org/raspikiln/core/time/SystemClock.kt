package org.raspikiln.core.time

import java.time.Clock
import java.time.Instant
import kotlin.time.Duration

interface SystemClock {
    companion object {
        fun systemUTC(): SystemClock = System()
    }

    fun instant(): Instant
    fun duration(from: Duration): Duration
}

private class System : SystemClock {
    private val clock = Clock.systemUTC()

    override fun instant(): Instant = clock.instant()
    override fun duration(from: Duration): Duration = from
}