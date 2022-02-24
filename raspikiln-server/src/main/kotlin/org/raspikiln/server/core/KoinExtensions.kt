package org.raspikiln.server.core

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun startKoinApplication(init: KoinApplication.() -> Unit) = startKoin { init() }.koin