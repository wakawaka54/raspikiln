package org.raspikiln.kiln.jackson

import com.fasterxml.jackson.databind.module.SimpleModule
import org.raspikiln.kiln.common.KilnLocation
import org.raspikiln.kiln.switches.SwitchState

class KilnConfigModule : SimpleModule() {
    init {
        addDeserializer(KilnLocation::class.java, KilnLocationDeserializer())
        addSerializer(SwitchState::class.java, SwitchStateSerializer())
        addDeserializer(SwitchState::class.java, SwitchStateDeserializer())
    }
}