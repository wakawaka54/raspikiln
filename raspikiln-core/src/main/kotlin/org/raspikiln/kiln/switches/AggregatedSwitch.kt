package org.raspikiln.kiln.switches

import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.common.intersectAll
import org.raspikiln.kiln.common.requiredAll

/**
 * Aggregate multiple switches.
 */
fun Iterable<Switch>.aggregate(): Switch = AggregatedSwitch(toList())

/**
 * Aggregate multiple switches if not empty.
 */
fun <C : Collection<Switch>> C.aggregateOrNull(): Switch? = takeIf { it.isNotEmpty() }?.aggregate()

/**
 * Switch facade which aggregates multiple switches.
 */
private class AggregatedSwitch(private val switches: List<Switch>) : Switch {

    override fun name(): String = "aggregated-switch"

    override fun switchType(): SwitchType = switches.requiredAll { it.switchType() }

    override fun locations(): Set<KilnLocation> = switches.intersectAll { it.locations() }

    override fun switchState(): SwitchState = switches.requiredAll { it.switchState() }

    override fun toggle(state: SwitchState) {
        switches.forEach { it.toggle(state) }
    }
}