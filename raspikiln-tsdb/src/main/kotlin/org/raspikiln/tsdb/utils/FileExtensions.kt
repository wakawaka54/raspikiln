package org.raspikiln.tsdb.utils

import java.io.File

fun File.makeDirectory() =
    when {
        isDirectory -> this
        mkdir() -> this
        else -> error("Unable to make directory ${this.name}")
    }