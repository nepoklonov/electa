fun <A, B, C> mapJoinFun(arg1: Map<A, B>, arg2: Map<B, C>): Map<A, C> =
    arg1.mapNotNull { it.key to (arg2[it.value] ?: return@mapNotNull null) }.toMap()