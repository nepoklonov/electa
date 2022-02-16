import rpc.RpcController

class TestController : RpcController {
    @Method(MethodType.GET)
    fun getExampleFun(arg: Int): Int {
        return arg * 5
    }
}