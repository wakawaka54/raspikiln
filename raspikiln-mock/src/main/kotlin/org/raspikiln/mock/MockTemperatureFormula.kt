package org.raspikiln.mock

import org.raspikiln.core.units.Temperature
import org.raspikiln.kiln.switches.SwitchState
import java.time.Clock

/**
 * Computes the temperature of a mock kiln.
 *
 * The formula is as follows:
 *  Given
 *      - Wattage of the kiln elements, e.g. 2000W
 *      - Energy produced by the kiln elements per second, simply Watts x Seconds = 2000W/s
 *      - Temperature of the room that the kiln is in, celsius e.g. 20C
 *      - Current temperature of the kiln, celsius e.g. 200C
 *      - Thermal efficiency of the kiln. 0 - 100%, e.g. 95% (how good the kiln is at not losing heat)
 *      - Cubic feet of the kiln, e.g. 1 ft^3
 *      - Completely made up number of how many Watts per second it takes to heat up air by 400C, e.g. 7_200_000 Watts
 *      - Differential inertia of kiln, made up value, it takes some time to heat up the elements and to cool off elements.
 *
 *  Take the current energy of the kiln, differential from room temperature so when the kiln is at room temperature
 *  then the energy in the kiln is 0W.
 *
 *  Compute the amount of energy lost since the last update, which is
 *      (Kiln Temperature - Room Temperature) x Cubic Volume x Watts lost per second per cubic foot x (1 minus thermal efficiency)
 *          x seconds
 *
 *  Compute the amount of energy gain since the last update, which is (if the heater is on)
 *      (Watts of kiln elements) x seconds
 *
 *  Compute the energy in the kiln,
 *      (Current Energy in the Kiln) + (Energy Gain - Energy Loss) + (Current Rate of Change x Differential Inertia)
 *
 *  Temperature in the kiln is:
 *      (Energy in the kiln) / (Cubic Volume x Wattage to raise temperature by 400) + Room Temperature
 */
class MockTemperatureFormula(private val parameters: Parameters) {
    private val initialEnergy = parameters.roomTemperature * parameters.wattsPerDegreeCelsius
    private var temperature = parameters.roomTemperature
    private var lastUpdate = Clock.systemUTC().millis()
    private var currentRateOfChange: Double = 0.0

    fun currentTemperature() = Temperature.Celsius(temperature)

    fun computeNext(state: ComputationState): Temperature {
        val now = Clock.systemUTC().millis()
        val elapsedTime = (now - lastUpdate).toDouble() / 1000.0
        val energyIn = state.energyIn(elapsedTime)
        val energyOut = energyOut(elapsedTime)
        val kilnEnergy = kilnEnergy() + energyIn - energyOut
        val kilnEnergyWithDifferential = kilnEnergy + currentRateOfChange * parameters.differentialInertia

        val boundedKilnEnergyWithDifferential = kilnEnergyWithDifferential.coerceAtLeast(0.0)

        currentRateOfChange = boundedKilnEnergyWithDifferential - kilnEnergy()
        lastUpdate = now
        temperature = (boundedKilnEnergyWithDifferential / parameters.wattsPerDegreeCelsius) + parameters.roomTemperature

        return currentTemperature()
    }

    private fun ComputationState.energyIn(timeElapsedSeconds: Double): Double =
        when (elementState) {
            SwitchState.On -> parameters.elementWattage * timeElapsedSeconds
            SwitchState.Off -> 0.0
        }

    private fun energyOut(timeElapsedSeconds: Double): Double {
        val tempDiff = temperature - parameters.roomTemperature
        val wattsLose = parameters.loseWattage * (1.0 - parameters.thermalEfficiency)
        return tempDiff * wattsLose * parameters.volume * timeElapsedSeconds
    }

    private fun kilnEnergy() = parameters.wattsPerDegreeCelsius * temperature - initialEnergy

    data class Parameters(
        val elementWattage: Int = 2000,
        val roomTemperature: Double = 20.0,
        val thermalEfficiency: Double = 0.95,
        val volume: Double = 1.0,
        val loseWattage: Double = 500.0,
        val differentialInertia: Double = 0.01,
        val wattsPerDegreeCelsius: Double = 18000.0
    )

    data class ComputationState(
        val elementState: SwitchState
    )
}