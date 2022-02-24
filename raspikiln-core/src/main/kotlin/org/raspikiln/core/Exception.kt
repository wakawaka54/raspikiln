package org.raspikiln.core

fun raspikilnException(message: String? = null, cause: Throwable? = null): Nothing = throw RaspikilnException(message, cause)

open class RaspikilnException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)