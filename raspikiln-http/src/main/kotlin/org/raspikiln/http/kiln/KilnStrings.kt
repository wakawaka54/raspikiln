package org.raspikiln.http.kiln

import org.raspikiln.http.core.strings.StringResolver
import org.raspikiln.http.core.strings.buildStringResult
import org.raspikiln.kiln.Kiln
import org.raspikiln.kiln.zones.KilnZoneName

fun StringResolver.zoneName(kiln: Kiln, zoneName: KilnZoneName) = buildStringResult {
    if (kiln.zones().size > 1) {
        +"Zone $zoneName"
    }
}

fun StringResolver.temperatureMetricDisplayName(kiln: Kiln, zoneName: KilnZoneName) = buildStringResult {
    +"Temperature"
    +zoneName(kiln, zoneName)
}

fun StringResolver.temperatureTargetMetricDisplayName(kiln: Kiln, zoneName: KilnZoneName) = buildStringResult {
    +"Target"
    +zoneName(kiln, zoneName)
}