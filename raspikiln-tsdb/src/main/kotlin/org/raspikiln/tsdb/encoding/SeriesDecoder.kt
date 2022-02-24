package org.raspikiln.tsdb.encoding

import org.raspikiln.tsdb.Datapoint

/**
 * Decodes a time series.
 */
interface SeriesDecoder {
    fun readNext(): Datapoint?
}

/**
 * Datapoint sequence.
 */
fun SeriesDecoder.sequence(): Sequence<Datapoint> = generateSequence { readNext() }