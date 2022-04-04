package controllers.rpc

import rpc.Method
import rpc.MethodType
import rpc.RpcController
import test.RPCTestEnumArgument
import test.RPCTestSealedArgument

class PostServerRPCTestController : RpcController, RPCTestController {
    private val implementation: RPCTestController = RPCTestControllerImplementation()

    @Method(MethodType.POST)
    override fun intProduct(arg1: Int, arg2: Int): Int = implementation.intProduct(arg1, arg2)

    @Method(MethodType.POST)
    override fun strSum(arg1: String, arg2: String): String = implementation.strSum(arg1, arg2)

    @Method(MethodType.POST)
    override fun listSum(arg1: MutableList<Int>, arg2: MutableList<String>): List<String> =
        implementation.listSum(arg1, arg2)

    @Method(MethodType.POST)
    override fun mapsJoin(arg1: Map<Int, String>, arg2: Map<String, Int>): Map<Int, Int> =
        implementation.mapsJoin(arg1, arg2)

    @Method(MethodType.POST)
    override fun nestedMapsJoin(arg1: Map<Map<Int, String>, Map<String, Int>>, arg2: Map<Map<String, Int>, Map<Int, String>>):
            Map<Map<Int, String>, Map<Int, String>> = implementation.nestedMapsJoin(arg1, arg2)

    @Method(MethodType.POST)
    override fun pairOfPair(arg1: Pair<Int, Int>, arg2: Pair<Char, Char>): Pair<Pair<Int, Int>, Pair<Char, Char>> =
        implementation.pairOfPair(arg1, arg2)

    @Method(MethodType.POST)
    override fun enumClassStr(arg1: RPCTestEnumArgument, arg2: RPCTestEnumArgument): String =
        implementation.enumClassStr(arg1, arg2)

    @Method(MethodType.POST)
    override fun sealedClassNum(arg1: RPCTestSealedArgument): Int = implementation.sealedClassNum(arg1)
}