package org.raspikiln.kiln.common

import java.util.concurrent.atomic.AtomicReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T : Any?> atomicValue(): ReadWriteProperty<Any, T> = AtomicValue(initial = null)

private class AtomicValue<T : Any?>(initial: T?) : ReadWriteProperty<Any, T> {

    private val reference = AtomicReference<T>(initial)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        reference.set(value)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = reference.get()
}