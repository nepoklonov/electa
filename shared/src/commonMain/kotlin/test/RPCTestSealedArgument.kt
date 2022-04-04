package test

import kotlinx.serialization.Serializable


@Serializable
sealed class RPCTestSealedArgument {
    @Serializable
    class Const(val num: Int) : RPCTestSealedArgument()
}