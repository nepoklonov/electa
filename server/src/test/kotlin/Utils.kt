fun <A, B, C> Map<A, B>.joinWith(other: Map<B, C>): Map<A, C> =
    this.mapNotNull { it.key to (other[it.value] ?: return@mapNotNull null) }.toMap()