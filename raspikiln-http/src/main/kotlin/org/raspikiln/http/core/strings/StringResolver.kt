package org.raspikiln.http.core.strings

/**
 * Strings resolver.
 */
fun strings(): StringResolver = Strings

/**
 * Provides strings.
 */
interface StringResolver {
    fun resolve(reference: StringReference): StringResult
}

private object Strings : StringResolver {
    override fun resolve(reference: StringReference): StringResult =
        StringResult.Unknown(reference)
}

data class StringReference(
    val name: String
)

sealed interface StringResult {
    companion object {
        fun maybeEmpty(value: String) =
            value.takeIf { it.isNotBlank() }?.let { Value(it) } ?: Empty
    }

    object Empty : StringResult {
        override fun maybeValue(): String? = null
    }

    data class Value(val value: String) : StringResult {
        override fun maybeValue(): String = value
    }

    class Unknown(reference: StringReference): StringResult  {
        private val value = "Unknown string ${reference.name}"
        override fun maybeValue(): String = value
    }

    fun maybeValue(): String?
    fun value(): String = maybeValue() ?: error("Unknown required string")
}