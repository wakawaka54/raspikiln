package org.raspikiln.core.units

sealed class Temperature(
    val unit: String,
    open val value: Double
) {
    data class Fahrenheit(override val value: Double) : Temperature(unit = LongName, value = value) {
        companion object {
            const val LongName = "fahrenheit"
            const val ShortName = "F"
            val Zero = Fahrenheit(0.0)
        }

        override fun math(fn: (Double) -> Double): Temperature = Fahrenheit(fn(value))
        override fun convertTo(temperature: Temperature): Temperature = temperature.celsius()
        override fun fahrenheit() = this
        override fun celsius(): Celsius = Celsius(5.0 / 9.0 * (value - 32))
        override fun toString(): String = "${value}F"
    }

    data class Celsius(override val value: Double) : Temperature(unit = LongName, value = value) {
        companion object {
            const val LongName = "celsius"
            const val ShortName = "C"
            val Zero = Celsius(0.0)
        }

        override fun math(fn: (Double) -> Double) = Celsius(fn(value))
        override fun convertTo(temperature: Temperature): Temperature = temperature.fahrenheit()
        override fun fahrenheit(): Fahrenheit = Fahrenheit(9.0 / 5.0 * value + 32)
        override fun celsius(): Celsius = this
        override fun toString(): String = "${value}C"
    }

    abstract fun math(fn: (Double) -> Double): Temperature
    abstract fun convertTo(temperature: Temperature): Temperature
    abstract fun fahrenheit(): Fahrenheit
    abstract fun celsius(): Celsius

    fun percentage(percentage: Double) = math { it * percentage }

    operator fun compareTo(other: Temperature) =
        celsius().value.compareTo(other.celsius().value)

    operator fun minus(temperature: Temperature): Temperature =
        math { it - convertTo(temperature).value }

    operator fun plus(temperature: Temperature): Temperature =
        math { it + convertTo(temperature).value }

    operator fun div(dividend: Double): Temperature =
        math { it / dividend }
}

fun <C : Collection<Temperature>> C.average() = Temperature.Celsius(map { it.celsius().value }.average())
