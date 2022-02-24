package org.raspikiln.rpi

import com.pi4j.Pi4J
import com.pi4j.library.pigpio.PiGpio
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider
import org.raspikiln.rpi.core.PiContext

interface RpiInitializer {
    companion object {
        fun native(): RpiInitializer = NativeRpiInitializer()
    }

    fun init(): PiContext
}

private class NativeRpiInitializer : RpiInitializer {
    override fun init(): PiContext {
        val piGpio = PiGpio.newNativeInstance()
        return Pi4J.newContextBuilder()
            .noAutoDetect()
            .add(PiGpioSpiProvider.newInstance(piGpio))
            .add(PiGpioDigitalOutputProvider.newInstance(piGpio))
            .build()
    }
}