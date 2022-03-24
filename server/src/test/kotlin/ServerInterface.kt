interface ServerInterface {
    fun intProduct(arg1: Int, arg2: Int): Int
    fun strSum(arg1: String, arg2: String): String
    fun listSum(arg1: MutableList<Int>, arg2: MutableList<String>): List<String>
    fun mapJoin(arg1: Map<Int, String>, arg2: Map<String, Int>): Map<Int, Int>
    fun mmJoin(
        arg1: Map<Map<Int, String>, Map<String, Int>>,
        arg2: Map<Map<String, Int>, Map<Int, String>>
    ): Map<Map<Int, Int>, Map<String, String>>

    fun pairOfPair(arg1: Pair<Int, Int>, arg2: Pair<Char, Char>): Pair<Pair<Int, Int>, Pair<Char, Char>>
    fun enumClassStr(arg1: EnumArguments, arg2: EnumArguments): String
    fun sealedClassNum(arg1: SealedArguments): Int
}