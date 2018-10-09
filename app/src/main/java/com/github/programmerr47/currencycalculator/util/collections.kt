package com.github.programmerr47.currencycalculator.util

fun <T> Iterable<T>.move(item: T, newPos: Int): List<T> =
        mutableListOf<T>().also {
            filterNotTo(it) { it == item }
            it.add(newPos, item)
        }

inline fun <K, V, R> Iterable<K>.mapFiltered(map: Map<K, V>, mapper: (K, V) -> R) =
        mapFilteredTo(map, ArrayList(), mapper)

inline fun <K, V, R, C : MutableCollection<in R>> Iterable<K>.mapFilteredTo(map: Map<K, V>, destination: C, mapper: (K, V) -> R): C {
    forEach { key ->
        map[key]?.let { value ->
            destination.add(mapper(key, value))
        }
    }
    return destination
}