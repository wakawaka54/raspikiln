package org.raspikiln.kiln.common

import kotlinx.coroutines.*

object Scopes {
    val Application = CoroutineScope(SupervisorJob())
}

object DispatcherScope {
    val ApplicationIo = CoroutineRuntime(scope = Scopes.Application, dispatcher = Dispatchers.IO)
}

data class CoroutineRuntime(val scope: CoroutineScope, val dispatcher: CoroutineDispatcher) {
    fun launch(block: suspend () -> Unit) =
        scope.launch {
            withContext(dispatcher) {
                block()
            }
        }
}