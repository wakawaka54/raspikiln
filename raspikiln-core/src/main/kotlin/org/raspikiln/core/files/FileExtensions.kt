package org.raspikiln.core.files

import java.io.File

fun File.makeParentDirectories() = apply { parentFile.mkdirs() }

fun File.makeDirectories() = apply { mkdirs() }