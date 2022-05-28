package org.raspikiln.simulation.runner

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.raspikiln.core.files.makeDirectories
import org.raspikiln.core.registerCoreModule
import org.raspikiln.simulation.SimMapper
import java.io.File
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class SimulationResults(private val resultDirectory: File) {
    companion object {
        private const val RESULT_FOLDER_SUFFIX = "-sim"
        private const val DATA_FILENAME = "data.json"

        fun create(parentDirectory: File): SimulationResults {
            val creationTime = Instant.now()
            return SimulationResults(
                File(parentDirectory, creationTime.toFolderString() + RESULT_FOLDER_SUFFIX)
                    .makeDirectories()
            )
        }

        fun loadAll(parentDirectory: File): List<SimulationResults> =
            (parentDirectory.listFiles() ?: emptyArray())
                .filter { it.name.endsWith(RESULT_FOLDER_SUFFIX) && it.isDirectory }
                .map { SimulationResults(it) }
                .toList()
    }

    fun writeData(data: SimulationResultData) = apply {
        File(resultDirectory, DATA_FILENAME).writeText(SimMapper.Json.writeValueAsString(data))
    }

    fun readData(): SimulationResultData =
        SimMapper.Json.readValue(
            File(resultDirectory, DATA_FILENAME),
            SimulationResultData::class.java
        )
}

private fun Instant.toFolderString() =
    DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
        .format(this.atZone(ZoneOffset.UTC))