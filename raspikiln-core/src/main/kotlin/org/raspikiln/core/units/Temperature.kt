package org.raspikiln.core.units

sealed class Temperature(
    val unit: String,
    open val value: Double
) {
    data class Fahrenheit(override val value: Double) : Temperature(unit = UnitNmae, value = value) {
        companion object {
            const val UnitNmae = "fahrenheit"
        }

        override fun fahrenheit() = this
        override fun celsius(): Celsius = Celsius(5.0 / 9.0 * (value - 32))
        override fun toString(): String = "${value}F"
    }

    data class Celsius(override val value: Double) : Temperature(unit = UnitNmae, value = value) {
        companion object {
            const val UnitNmae = "celsius"
        }

        override fun fahrenheit(): Fahrenheit = Fahrenheit(9.0 / 5.0 * value + 32)
        override fun celsius(): Celsius = this
        override fun toString(): String = "${value}C"
    }

    abstract fun fahrenheit(): Fahrenheit
    abstract fun celsius(): Celsius

    operator fun compareTo(other: Temperature) =
        celsius().value.compareTo(other.celsius().value)
}

fun <C : Collection<Temperature>> C.average() = Temperature.Celsius(map { it.celsius().value }.average())
