package rpc

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.*


private val json = Json { isLenient = true }

fun Route.rpc(controllerClass: KClass<out RpcController>) {
    val instance = controllerClass.createInstance()

    controllerClass.declaredMemberFunctions.forEach { function ->
        when (function.findAnnotation<Method>()?.type) {
            MethodType.GET -> get(function.name) {
                val parameters = call.request.queryParameters.toMap().mapValues {
                    it.value.singleOrNull() ?: return@get call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "Multiple or empty values are not supported"
                    )
                }
                val serializedResult = processRequest(instance, function, parameters)
                call.respond(serializedResult)
            }
            MethodType.POST -> post(function.name) {
                val parameters = json.decodeFromString<Map<String, String>>(call.receiveText())
                val serializedResult = processRequest(instance, function, parameters)
                call.respond(serializedResult)
            }
            null -> error("Cannot find annotation")
        }
    }
}

suspend fun processRequest(
    instance: RpcController,
    function: KFunction<*>,
    serializedArguments: Map<String, String>
): String {
    val deserializedArguments = deserializeArguments(function, serializedArguments)
    val result = function.callSuspend(instance, *deserializedArguments.toTypedArray())
    return Json.encodeToString(serializer(function.returnType), result)
}

fun deserializeArguments(
    function: KFunction<*>,
    queryParameters: Map<String, String>
): List<Any?> {
    return function.valueParameters.map { param ->
        val argumentValue = queryParameters[param.name] ?: error("parameter '${param.name}' is missing")
        Json.decodeFromString(serializer(param.type), argumentValue)
    }
}