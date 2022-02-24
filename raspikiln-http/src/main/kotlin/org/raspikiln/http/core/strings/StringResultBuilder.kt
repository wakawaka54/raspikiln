package org.raspikiln.http.core.strings

fun buildStringResult(seperator: String = " ", init: StringResultBuilder.() -> Unit): StringResult =
    StringResultBuilder(seperator).apply(init).build()

class StringResultBuilder(private val seperator: String = " ") {
    private val segments: MutableList<StringResult> = mutableListOf()

    operator fun String.unaryPlus() {
        segments.add(StringResult.Value(this))
    }

    operator fun StringResult.unaryPlus() {
        segments.add(this)
    }

    fun build(): StringResult =
        StringResult.maybeEmpty(
            segments.mapNotNull { it.maybeValue() }.joinToString(seperator)
        )
}