package utils

fun <A, B, C> Map<A, B>.joinWith(other: Map<B, C>): Map<A, C> = mapNotNull {
    it.key to (other[it.value] ?: return@mapNotNull null)
}.toMap()