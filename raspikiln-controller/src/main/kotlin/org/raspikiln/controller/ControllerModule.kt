package org.raspikiln.controller

import org.koin.dsl.module
import org.raspikiln.jobs.onGlobal

val ControllerModule = module {
    single(createdAtStart = true) { TemperatureControllerJob(kiln = get(), temperatureController = get()).onGlobal() }
    single { KilnTemperatureController() }
}