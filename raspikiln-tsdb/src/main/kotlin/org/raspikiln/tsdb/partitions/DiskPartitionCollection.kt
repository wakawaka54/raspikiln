package org.raspikiln.tsdb.partitions

import org.raspikiln.tsdb.MetricIdentifier
import org.raspikiln.tsdb.utils.DelayQueue
import org.raspikiln.tsdb.utils.TimestampRange
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque
import kotlin.time.Duration

/**
 * Collection of disk partitions.
 */
class DiskPartitionCollection(
    initial: List<DiskPartition>,
    deleteDelay: Duration
) {
    private val diskPartitions: BlockingDeque<DiskPartition> = LinkedBlockingDeque(initial.sorted())
    private val deleteQueue: DelayQueue<DiskPartition> = DelayQueue(delayAmount = deleteDelay)

    fun add(partition: DiskPartition) {
        diskPartitions.addFirst(partition)
    }

    fun query(metricName: MetricIdentifier, timestampRange: TimestampRange) = sequence {
        diskPartitions.descendingIterator().forEach { diskPartition ->
            yieldAll(diskPartition.query(metricName, timestampRange))
        }
    }

    fun purgeExpired() {
        synchronized(diskPartitions) {
            while (diskPartitions.peekLast()?.expired() == true) {
                diskPartitions.removeLast().queueDelete()
            }
        }

        purgeDeleteQueue()
    }

    fun purgeAll() {
        synchronized(diskPartitions) {
            while (diskPartitions.isNotEmpty()) {
                diskPartitions.removeLast().queueDelete()
            }
        }

        purgeDeleteQueue()
    }

    private fun purgeDeleteQueue() {
        deleteQueue.nextSequence().forEach { it.delete() }
    }

    private fun DiskPartition.queueDelete() = deleteQueue.add(this)

    private fun List<DiskPartition>.sorted() = sortedByDescending { it.timestampRange().endInclusive }
}