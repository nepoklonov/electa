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
import rpc.RpcController
import java.io.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.*


fun prepareArguments(
    valueParameters: List<KParameter>, queryParameters: Map<String, String>, instance: RpcController
): MutableList<Any?> {
    val args = mutableListOf<Any?>(instance)
    valueParameters.mapTo(args) { param ->
        Json { isLenient = true }.decodeFromString(
            serializer(param.type), queryParameters[param.name.toString()] ?: error("param is missing")
        )
    }
    return args
}

suspend fun processRequest(
    function: KFunction<*>, serializedParameters: Map<String, String>, instance: RpcController
): Serializable {
    val deserializedParameters = prepareArguments(function.valueParameters, serializedParameters, instance)
    val result = function.callSuspend(*deserializedParameters.toTypedArray())
    return Json.encodeToString(serializer(function.returnType), result)
}

fun Route.rpc(controllerClass: KClass<out RpcController>) {
    val instance = controllerClass.createInstance()

    controllerClass.declaredMemberFunctions.forEach { function ->

        when (function.findAnnotation<Method>()?.type) {
            MethodType.GET -> get(function.name) {
                val parameters = call.request.queryParameters.toMap().mapValues {
                    it.value.singleOrNull() ?: return@get call.respond(
                        status = HttpStatusCode.BadRequest, message = "Multiple or empty values are not supported"
                    )
                }
                val serializedResult = processRequest(function, parameters, instance)
                call.respond(serializedResult)
            }
            MethodType.POST -> post(function.name) {
                val parameters = Json { isLenient = true }.decodeFromString(
                    MapSerializer(String.serializer(), String.serializer()), call.receiveText()
                )
                val serializedResult = processRequest(function, parameters, instance)
                call.respond(serializedResult)
            }
            null -> error("Cannot find annotation")
        }
    }
}