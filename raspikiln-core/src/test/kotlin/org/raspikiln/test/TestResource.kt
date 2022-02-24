package org.raspikiln.test

object TestResource {
    private val loader = this::class.java.classLoader

    fun readStream(path: String) = requireNotNull(loader.getResourceAsStream(path)) { "Missing resource $path" }

    fun config(name: String) = readStream("config/$name.yml")
}