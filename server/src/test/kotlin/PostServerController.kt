import rpc.Method
import rpc.MethodType
import rpc.RpcController

class PostServerController : RpcController, TestRpcController {
    private val implementation: TestRpcController = ImplementationFunsForRPCTests()

    @Method(MethodType.POST)
    override fun intProduct(arg1: Int, arg2: Int): Int = implementation.intProduct(arg1, arg2)

    @Method(MethodType.POST)
    override fun strSum(arg1: String, arg2: String): String = implementation.strSum(arg1, arg2)

    @Method(MethodType.POST)
    override fun listSum(arg1: MutableList<Int>, arg2: MutableList<String>): List<String> =
        implementation.listSum(arg1, arg2)

    @Method(MethodType.POST)
    override fun mapJoin(arg1: Map<Int, String>, arg2: Map<String, Int>): Map<Int, Int> =
        implementation.mapJoin(arg1, arg2)

    @Method(MethodType.POST)
    override fun mmJoin(arg1: Map<Map<Int, String>, Map<String, Int>>, arg2: Map<Map<String, Int>, Map<Int, String>>):
            Map<Map<Int, String>, Map<Int, String>> = implementation.mmJoin(arg1, arg2)

    @Method(MethodType.POST)
    override fun pairOfPair(arg1: Pair<Int, Int>, arg2: Pair<Char, Char>): Pair<Pair<Int, Int>, Pair<Char, Char>> =
        implementation.pairOfPair(arg1, arg2)

    @Method(MethodType.POST)
    override fun enumClassStr(arg1: EnumArgumentsForRPCTests, arg2: EnumArgumentsForRPCTests): String =
        implementation.enumClassStr(arg1, arg2)

    @Method(MethodType.POST)
    override fun sealedClassNum(arg1: SealedArgumentsForRPCTests): Int = implementation.sealedClassNum(arg1)
}