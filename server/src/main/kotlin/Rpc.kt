import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import rpc.Method
import rpc.MethodType
import rpc.RpcController
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.*


private val json = Json { isLenient = true }

fun prepareArguments(
    instance: RpcController,
    function: KFunction<*>,
    queryParameters: Map<String, String>
): MutableList<Any?> {
    return function.parameters.mapTo(mutableListOf(instance)) { param ->
        val argumentValue = queryParameters[param.name] ?: error("parameter is missing")
        Json.decodeFromString(serializer(param.type),argumentValue)
    }
}

suspend fun processRequest(
    instance: RpcController,
    function: KFunction<*>,
    serializedArguments: Map<String, String>
): String {
    val deserializedArguments = prepareArguments(instance, function, serializedArguments)
    val result = function.callSuspend(*deserializedArguments.toTypedArray())
    return Json.encodeToString(serializer(function.returnType), result)
}

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
                val parameters = json.decodeFromString(
                    MapSerializer(String.serializer(), String.serializer()), call.receiveText()
                )
                val serializedResult = processRequest(instance, function, parameters)
                call.respond(serializedResult)
            }
            null -> error("Cannot find annotation")
        }
    }
}