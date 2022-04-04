package controllers.rpc

import test.RPCTestEnumArgument
import test.RPCTestSealedArgument

interface RPCTestController {
    fun intProduct(arg1: Int, arg2: Int): Int
    fun strSum(arg1: String, arg2: String): String
    fun listSum(arg1: MutableList<Int>, arg2: MutableList<String>): List<String>
    fun mapsJoin(arg1: Map<Int, String>, arg2: Map<String, Int>): Map<Int, Int>
    fun nestedMapsJoin(
        arg1: Map<Map<Int, String>, Map<String, Int>>,
        arg2: Map<Map<String, Int>, Map<Int, String>>
    ): Map<Map<Int, String>, Map<Int, String>>

    fun pairOfPair(arg1: Pair<Int, Int>, arg2: Pair<Char, Char>): Pair<Pair<Int, Int>, Pair<Char, Char>>
    fun enumClassStr(arg1: RPCTestEnumArgument, arg2: RPCTestEnumArgument): String
    fun sealedClassNum(arg1: RPCTestSealedArgument): Int
}