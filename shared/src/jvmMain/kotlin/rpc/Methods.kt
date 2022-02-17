package rpc

enum class MethodType {
    GET,
    POST
}

annotation class Method(val type: MethodType)
