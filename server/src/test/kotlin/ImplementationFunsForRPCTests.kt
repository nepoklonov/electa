class ImplementationFunsForRPCTests : TestRpcController {
    override fun intProduct(arg1: Int, arg2: Int): Int = arg1 * arg2

    override fun strSum(arg1: String, arg2: String): String = arg1 + arg2

    override fun listSum(arg1: MutableList<Int>, arg2: MutableList<String>): List<String> =
        arg1.map { it.toString() } + arg2

    override fun mapJoin(arg1: Map<Int, String>, arg2: Map<String, Int>): Map<Int, Int> = arg1.joinWith(arg2)

    override fun mmJoin(arg1: Map<Map<Int, String>, Map<String, Int>>, arg2: Map<Map<String, Int>, Map<Int, String>>):
            Map<Map<Int, String>, Map<Int, String>> = arg1.joinWith(arg2)

    override fun pairOfPair(arg1: Pair<Int, Int>, arg2: Pair<Char, Char>): Pair<Pair<Int, Int>, Pair<Char, Char>> =
        arg1 to arg2

    override fun enumClassStr(arg1: EnumArgumentsForRPCTests, arg2: EnumArgumentsForRPCTests): String = "${arg1.par}${arg2.par}"

    override fun sealedClassNum(arg1: SealedArgumentsForRPCTests): Int = when (arg1) {
        is SealedArgumentsForRPCTests.Const -> arg1.num
    }
}