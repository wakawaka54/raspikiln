package org.raspikiln.rpi.core

import com.pi4j.context.Context
import com.pi4j.io.gpio.digital.DigitalConfigBuilder
import com.pi4j.io.gpio.digital.DigitalInputConfigBuilder
import com.pi4j.io.gpio.digital.DigitalOutput
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder
import com.pi4j.io.spi.Spi
import com.pi4j.io.spi.SpiConfigBuilder

typealias PiContext = Context

/**
 * DSL SPI builder.
 */
fun Context.spi(init: SpiConfigBuilder.() -> Unit): Spi = create(Spi.newConfigBuilder(this).apply(init))

/**
 * DSL Digital output builder.
 */
fun Context.digitalOutput(init: DigitalOutputConfigBuilder.() -> Unit): DigitalOutput =
    create(DigitalOutputConfigBuilder.newInstance(this).apply(init).build())