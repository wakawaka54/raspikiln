package org.raspikiln.server.core

import org.koin.dsl.module
import org.raspikiln.tsdb.StandardTimeSeriesDB
import org.raspikiln.tsdb.TimesSeriesDB
import java.io.File
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

val DatabaseModule = module {
    single {
        TimesSeriesDB.create(StandardTimeSeriesDB.Options(
            directory = File("data/ts"),
            partitionDuration = 15.minutes,
            partitionCapacity = 1_000,
            retention = 48.hours,
            deleteDelayTime = 15.minutes
        ))
    }
}