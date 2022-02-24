package org.raspikiln.kiln.common

fun <T, R> Collection<T>.requiredAll(transform: (T) -> R): R =
    requireNotNull(requiredAllOrNull(transform)) { "Collection was empty." }

fun <T, R> Collection<T>.requiredAllOrNull(transform: (T) -> R): R? =
    map(transform).reduceOrNull { acc, next ->
        require(acc == next) { "Required $acc but was $next" }; acc
    }

fun <T, R> Collection<T>.intersectAll(transform: (T) -> Collection<R>): Set<R> =
    requireNotNull(
        intersectAllOrNull(transform)
    ) { "Intersection was null." }

fun <T, R> Collection<T>.intersectAllOrNull(transform: (T) -> Collection<R>): Set<R>? =
    map(transform).reduceOrNull { intersects, next -> intersects.intersect(next.toSet()) }?.toSet()