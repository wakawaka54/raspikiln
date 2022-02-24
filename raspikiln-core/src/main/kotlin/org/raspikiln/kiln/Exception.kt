package org.raspikiln.kiln

import org.raspikiln.core.RaspikilnException

fun kilnProviderError(message: String? = null, cause: Throwable? = null): Nothing = throw KilnProviderException(message, cause)

open class KilnProviderException(message: String? = null, cause: Throwable? = null) : RaspikilnException(message, cause)