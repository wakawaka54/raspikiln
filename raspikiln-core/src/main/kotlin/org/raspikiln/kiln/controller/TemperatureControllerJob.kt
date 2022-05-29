package org.raspikiln.kiln.controller

import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.switches.SwitchState
import kotlin.time.Duration

class TemperatureControllerJob(
    private val kiln: Kiln,
    private val controller: TemperatureController
)  {
    /**
    private val dutyCycle = controller.dutyCycle()
    private val zones = controller.locations().map { kiln.zone(it) }
    private val states = LinkedBlockingQueue<ZoneHeaterState>(2)

    override val name: String = "KilnTemperatureController${controller.name()}"

    override fun first(): ZonedDateTime? = ZonedDateTime.now()

    override fun next(last: ZonedDateTime): ZonedDateTime =
        nextQueueSchedule() ?: computeNext() ?: nextDutyCycle()

    override fun run() {
        val head = states.poll() ?: return

        zones.forEach { it.heaterState(head.switchState) }
    }

    private fun head() = states.firstOrNull()

    private fun nextQueueSchedule(): ZonedDateTime? {
        val head = head() ?: return null
        return ZonedDateTime.now().plus(head.duration)
    }

    private fun computeNext(): ZonedDateTime? {
        val target = target() ?: return null

        val controllerOutput = controller.evaluate(
            ControllerInput(
                setpoint = target,
                temperature = zones.temperatureSensors().average(),
                heaterState = zones.heaterState(),
                dutyCycle = dutyCycle
            )
        )

        val outputs = controllerOutput.outputStates()

        states.addAll(outputs)

        return nextQueueSchedule()
    }

    private fun nextDutyCycle() = ZonedDateTime.now().plus(dutyCycle)

    private fun ControllerOutput.outputStates(): LinkedList<ZoneHeaterState> =
        listOf(
            SwitchState.On to heaterOn,
            SwitchState.Off to heaterOff
        )
        .map { (switchState, duration) -> ZoneHeaterState(switchState, duration) }
        .filter { it.duration > 0.seconds }
        .toCollection(LinkedList())

    private fun target() = zones.mapNotNull { it.target() }.takeIf { it.isNotEmpty() }?.average()
    **/
}

private data class ZoneHeaterState(
    val switchState: SwitchState,
    val duration: Duration
)