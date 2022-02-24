package org.raspikiln.mock

import kotlin.random.Random

object Arbritrary {
    private val random = Random.Default

    fun double(from: Double, to: Double) = random.nextDouble(from, to)
}