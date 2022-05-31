package org.raspikiln.server.config

import org.koin.dsl.module

val ConfigModule = module {
    single { ConfigFileReader(mapper = get()) }
}