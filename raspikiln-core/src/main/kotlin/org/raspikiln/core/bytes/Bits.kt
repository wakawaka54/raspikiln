package org.raspikiln.core.bytes

fun ByteArray.asBits() = Bits(this)

/**
 * Bits utility type.
 */
class Bits(private val byteArray: ByteArray) {

    /**
     * Gets a bit from the byte array from the left hand side.
     *
     * 1101 0010 1000 0000
     *
     * getBit(0) = 1
     * getBit(7) = 0
     * getBit(8) = 1
     */
    fun getBit(index: Int): Int = byteArray[index / 8].getBit(index % 8)

    /**
     * Returns getBit with a boolean value, if true that means the bit is 1, 0 bit is false.
     */
    fun getBitBoolean(index: Int) = getBit(index) == 1

    fun getBits(range: IntRange): Sequence<Int> = range.asSequence().map { getBit(it) }

    /**
     * Gets an MSB int, with a sign bit at the front.
     */
    fun getMsbInt(range: IntRange): Int {
        val sign = getBit(range.first)
        val bits = getBits((range.first + 1)..range.last).toList()
        val bitsSize = bits.size - 1
        return bits
            .foldIndexed(0) { index, acc, next ->
                acc + ((next xor sign) shl (bitsSize - index))
            }
    }

    /**
     * Gets an MSB int, with a sign bit at the front.
     */
    fun getMsbUInt(range: IntRange): UInt {
        val bits = getBits(range).toList()
        val bitsSize = bits.size - 1
        return bits
            .foldIndexed(0U) { index, acc, next ->
                acc + (next shl (bitsSize - index)).toUInt()
            }
    }
}

/**
 * Gets the bit in a byte from the left hand side, for example.
 *
 * 25 -> 11001
 * getBit(0) = 1
 * getBit(1) = 1
 * getBit(2) = 0
 */
fun Byte.getBit(index: Int): Int = (toInt() shr (7 - index)) and 1
