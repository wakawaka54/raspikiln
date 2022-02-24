package org.raspikiln.tsdb.encoding

import org.raspikiln.tsdb.Datapoint
import java.io.Closeable

interface SeriesEncoder : Closeable {
    fun write(datapoint: Datapoint)
}