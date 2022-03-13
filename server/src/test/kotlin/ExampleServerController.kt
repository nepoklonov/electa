import rpc.Method
import rpc.MethodType
import rpc.RpcController

class TestController : RpcController {

    @Method(MethodType.GET)
    fun getInt(arg1: Int, arg2: Int): Int {
        return arg1 * arg2
    }

    @Method(MethodType.POST)
    fun postInt(arg1: Int, arg2: Int): Int {
        return arg1 * arg2
    }

    @Method(MethodType.GET)
    fun getStr(arg1: String, arg2: String): String {
        return arg1 + arg2
    }

    @Method(MethodType.POST)
    fun postStr(arg1: String, arg2: String): String {
        return arg1 + arg2
    }

    @Method(MethodType.GET)
    fun getList(arg1: MutableList<Int>, arg2: MutableList<String>): MutableList<String> {
        val ans: MutableList<String> = mutableListOf()
        for(i in 0..4) {
            ans += arg1[i].toString()
            ans += arg2[i]
        }
        return ans
    }

    @Method(MethodType.POST)
    fun postList(arg1: MutableList<Int>, arg2: MutableList<String>): MutableList<String> {
        val ans: MutableList<String> = mutableListOf()
        for(i in 0..4) {
            ans += arg1[i].toString()
            ans += arg2[i]
        }
        return ans
    }

    @Method(MethodType.GET)
    fun getMap(arg1: Map<Int, String>, arg2: Map<String, Int>): Map<String, String> {
        return arg1.mapKeys{it.toString()} + arg2.mapValues{it.toString()}
    }

    @Method(MethodType.POST)
    fun postMap(arg1: Map<Int, String>, arg2: Map<String, Int>): Map<String, String> {
        return arg1.mapKeys{it.toString()} + arg2.mapValues{it.toString()}
    }

    @Method(MethodType.GET)
    fun getMM(arg1: Map<Map<Int, String>, Map<String,Int>>, arg2: Map<Map<String, Int>, Map<Int,String>>): Map<Map<String, String>, Map<String, String>> {
        val arg1StrKey: Map<String, String> = arg1.keys.elementAt(0).mapKeys{it.toString()}
        val arg1StrValue: Map<String, String> = arg1.values.elementAt(0).mapValues{it.toString()}
        val arg2StrKey: Map<String, String> = arg2.keys.elementAt(0).mapValues{it.toString()}
        val arg2StrValue: Map<String, String> = arg2.values.elementAt(0).mapKeys{it.toString()}

        val map1: Map<Map<String, String>, Map<String, String>> = mapOf(arg1StrKey to arg1StrValue)
        val map2: Map<Map<String, String>, Map<String, String>> = mapOf(arg2StrKey to arg2StrValue)

        return map1 + map2
    }

    @Method(MethodType.POST)
    fun postMM(arg1: Map<Map<Int, String>, Map<String,Int>>, arg2: Map<Map<String, Int>, Map<Int,String>>): Map<Map<String, String>, Map<String, String>> {
        val arg1StrKey: Map<String, String> = arg1.keys.elementAt(0).mapKeys{it.toString()}
        val arg1StrValue: Map<String, String> = arg1.values.elementAt(0).mapValues{it.toString()}
        val arg2StrKey: Map<String, String> = arg2.keys.elementAt(0).mapValues{it.toString()}
        val arg2StrValue: Map<String, String> = arg2.values.elementAt(0).mapKeys{it.toString()}

        val map1: Map<Map<String, String>, Map<String, String>> = mapOf(arg1StrKey to arg1StrValue)
        val map2: Map<Map<String, String>, Map<String, String>> = mapOf(arg2StrKey to arg2StrValue)

        return map1 + map2
    }

    @Method(MethodType.GET)
    fun getPair(arg1: Pair<Int, Int>, arg2: Pair<Char, Char>): Pair<String, String> {
        return Pair("${arg1.first}${arg2.first}", "${arg1.second}${arg2.second}")
    }

    @Method(MethodType.POST)
    fun postPair(arg1: Pair<Int, Int>, arg2: Pair<Char, Char>): Pair<String, String> {
        return Pair("${arg1.first}${arg2.first}", "${arg1.second}${arg2.second}")
    }

    @Method(MethodType.GET)
    fun getEnumClass(arg1: EnumArguments, arg2: EnumArguments): String {
        return "${arg1.par}${arg2.par}"
    }

    @Method(MethodType.POST)
    fun postEnumClass(arg1: EnumArguments, arg2: EnumArguments): String {
        return "${arg1.par}${arg2.par}"
    }

    @Method(MethodType.GET)
    fun getSealedClass(arg1: SealedArguments): Int = when(arg1) {
        is SealedArguments.Const -> arg1.num
    }

    @Method(MethodType.POST)
    fun postSealedClass(arg1: SealedArguments): Int = when(arg1) {
        is SealedArguments.Const -> arg1.num
    }
}