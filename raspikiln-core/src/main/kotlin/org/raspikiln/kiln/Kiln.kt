package org.raspikiln.kiln

import mu.KotlinLogging
import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.bridge.KilnBridge
import org.raspikiln.kiln.common.atomicValue
import org.raspikiln.kiln.historical.KilnHistoricalStore
import org.raspikiln.kiln.programs.*
import org.raspikiln.kiln.programs.types.ProgramDefinition
import org.raspikiln.kiln.switches.SwitchState
import java.util.concurrent.atomic.AtomicBoolean

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

    private val controllers = bridge.controllers()
    private var program: Program? by atomicValue()

    fun start() = apply {
        if (!started.getAndSet(true)) {

        }
    }

    /**
     * Get the current state of the kiln.
     */
    fun state(): KilnState {
        /**
        val zones = zones().map { it.state() }
        return KilnState(
            zones = zones,
            armState = zones.map { it.armingSwitchState }.anyOn() ?: SwitchState.Off,
            temperature = zones.map { it.temperature }.average(),
            temperatureTarget = zones.mapNotNull { it.temperatureSetpoint }.maxByOrNull { it.value }
        )
        **/
        return KilnState(
            armState = SwitchState.Off,
            temperature = Temperature.Celsius.Zero,
            temperatureTarget = null
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