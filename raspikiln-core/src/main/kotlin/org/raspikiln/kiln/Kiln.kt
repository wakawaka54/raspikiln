package org.raspikiln.kiln

import mu.KotlinLogging
import org.raspikiln.core.units.average
import org.raspikiln.jobs.onGlobal
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.bridge.kilnZones
import org.raspikiln.kiln.common.atomicValue
import org.raspikiln.kiln.controller.TemperatureControllerJob
import org.raspikiln.kiln.historical.HistoricalArchiveJob
import org.raspikiln.kiln.historical.KilnHistoricalStore
import org.raspikiln.kiln.programs.*
import org.raspikiln.kiln.programs.types.ProgramDefinition
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.switches.anyOn
import org.raspikiln.kiln.zones.KilnZoneName
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.logger {  }

/**
 * Kiln abstraction.
 */
class Kiln(
    private val bridge: KilnBridge,
    private val historicalStore: KilnHistoricalStore
) {
    private val started = AtomicBoolean(false)
    private val programFactory = ProgramFactory()

    private val zones = bridge.kilnZones().associateBy { it.name }
    private val controllers = bridge.controllers()
    private var program: Program? by atomicValue()

    fun start() = apply {
        if (!started.getAndSet(true)) {
            HistoricalArchiveJob(
                kiln = this,
                options = HistoricalArchiveJob.Options(interval = 3.seconds)
            ).onGlobal()

            ProgramEvaluatorJob(
                kiln = this,
                options = ProgramEvaluatorJobOptions(interval = 1.seconds)
            ).onGlobal()

            controllers.forEach { controller ->
                TemperatureControllerJob(
                    kiln = this,
                    controller = controller
                ).onGlobal()
            }
        }
    }

    fun zone(zoneName: KilnZoneName) = zones[zoneName] ?: error("Kiln did not have zone $zoneName")

    /**
     * List of zones.
     */
    fun zones() = zones.values

    /**
     * Get the current state of the kiln.
     */
    fun state(): KilnState {
        val zones = zones().map { it.state() }
        return KilnState(
            zones = zones,
            armState = zones.map { it.armingSwitchState }.anyOn() ?: SwitchState.Off,
            temperature = zones.map { it.temperature }.average(),
            temperatureTarget = zones.mapNotNull { it.temperatureSetpoint }.maxByOrNull { it.value }
        )
    }

    /**
     * Historical kiln data.
     */
    fun historical() = historicalStore

    fun startProgram(definition: ProgramDefinition) {
        logger.info { "Starting program [$definition]" }
        val program = programFactory.create(kiln = this, definition)
        this.program = program.start()
    }

    fun endProgram() {
        program?.end()
        program = null
    }

    /**
     * Current executing program.
     */
    fun currentProgram(): Program? = program

    /**
     * The kiln definition.
     */
    fun definition() = bridge
}

fun Kiln.zoneNames() = zones().map { it.name }