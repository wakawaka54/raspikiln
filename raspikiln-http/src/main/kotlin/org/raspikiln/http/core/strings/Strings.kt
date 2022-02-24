package org.raspikiln.http.core.strings

fun StringResolver.empty() = StringResult.Empty

fun StringResolver.value(value: String) = StringResult.Value(value)

fun List<StringResult>.join(seperator: String = "  ") =
    StringResult.Value(
        mapNotNull { it.maybeValue() }.joinToString(seperator)
    )