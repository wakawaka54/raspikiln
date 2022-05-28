package org.raspikiln.kiln.sensors

import org.raspikiln.core.services.Service

interface Sensor : Service {
    fun name(): String
}