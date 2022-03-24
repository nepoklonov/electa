class Implementation : ServerInterface {
    override fun intProduct(arg1: Int, arg2: Int): Int = arg1 * arg2

    override fun strSum(arg1: String, arg2: String): String = arg1 + arg2

    override fun listSum(arg1: MutableList<Int>, arg2: MutableList<String>): List<String> =
        arg1.map { it.toString() } + arg2

    override fun mapJoin(arg1: Map<Int, String>, arg2: Map<String, Int>): Map<Int, Int> = mapJoinFun(arg1, arg2)

    override fun mmJoin(arg1: Map<Map<Int, String>, Map<String, Int>>, arg2: Map<Map<String, Int>, Map<Int, String>>):
            Map<Map<Int, Int>, Map<String, String>> {
        val map1: Map<Int, Int> = mapJoinFun(arg1.keys.elementAt(0), arg1.values.elementAt(0))
        val map2: Map<String, String> = mapJoinFun(arg2.keys.elementAt(0), arg2.values.elementAt(0))

        return mapOf(map1 to map2)
    }

    override fun pairOfPair(arg1: Pair<Int, Int>, arg2: Pair<Char, Char>): Pair<Pair<Int, Int>, Pair<Char, Char>> =
        Pair(arg1, arg2)

    override fun enumClassStr(arg1: EnumArguments, arg2: EnumArguments): String = "${arg1.par}${arg2.par}"

    override fun sealedClassNum(arg1: SealedArguments): Int = when (arg1) {
        is SealedArguments.Const -> arg1.num
    }
}