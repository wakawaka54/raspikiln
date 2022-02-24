package org.raspikiln.kiln.historical

import org.raspikiln.kiln.KilnState
import org.raspikiln.kiln.common.MetricName
import org.raspikiln.kiln.switches.SwitchState
import org.raspikiln.kiln.zones.ZoneState
import org.raspikiln.tsdb.Datapoint
import org.raspikiln.tsdb.Measurement
import org.raspikiln.tsdb.MetricIdentifier
import org.raspikiln.tsdb.TimesSeriesDB

class KilnHistoricalTimeSeriesStore(private val timeSeriesDB: TimesSeriesDB) : KilnHistoricalStore {
    override fun record(state: KilnState) {
        with(state) {
            timeSeriesDB.write(
                state.zones.flatMap {
                    listOfNotNull(
                        it.recordTemperature(),
                        it.recordSetpoint()
                    )
                } + recordTemperature()
            )
        }
    }

    private fun KilnState.recordTemperature() =
        Measurement(
            metric = MetricIdentifier(name = "temperature"),
            datapoint = Datapoint(value = temperature.celsius().value)
        )

    private fun ZoneState.recordTemperature() =
        Measurement(
            metric = MetricName.temperature(name),
            datapoint = Datapoint(value = temperature.celsius().value)
        )

    private fun ZoneState.recordSetpoint(): Measurement? {
        val temperatureSetpoint = temperatureSetpoint?.celsius()?.value ?: return null
        return Measurement(
            metric = MetricName.temperatureTarget(name),
            datapoint = Datapoint(value = temperatureSetpoint)
        )
    }
}