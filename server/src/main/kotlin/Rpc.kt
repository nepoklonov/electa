import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure


@OptIn(InternalSerializationApi::class)
fun getArgs(
    valueParameters: List<KParameter>,
    queryParameters: Map<String, String>,
    instance: RpcController
): MutableList<Any?> {
    val args = mutableListOf<Any?>(instance)
    valueParameters.mapTo(args) { param ->
        Json { isLenient = true }.decodeFromString(
            param.type.jvmErasure.serializer(),
            queryParameters[param.name.toString()] ?: error("param is missing")
        )
    }
    return args
}

@OptIn(InternalSerializationApi::class)
@Suppress("UNCHECKED_CAST")
fun serializeResult(function: KFunction<*>, result: Any?): Serializable {
    return if (function.returnType.arguments.isNotEmpty()) {
        when {
            function.returnType.isSubtypeOf(List::class.createType(function.returnType.arguments)) -> Json.encodeToString(
                ListSerializer(function.returnType.arguments.first().type?.jvmErasure!!.serializer() as KSerializer<Any>),
                result as List<Any>
            )
            function.returnType.isSubtypeOf(Set::class.createType(function.returnType.arguments)) -> Json.encodeToString(
                SetSerializer(function.returnType.arguments.first().type?.jvmErasure!!.serializer() as KSerializer<Any>),
                result as Set<Any>
            )
            else -> SerializationException("Method must return either List<R> or Set<R>, but it returns ${function.returnType}")
        }
    } else {
        Json.encodeToString(result!!::class.serializer() as KSerializer<Any>, result)
    }
}

suspend fun processRequest(
    function: KFunction<*>,
    serializedParameters: Map<String, String>,
    instance: RpcController
): Serializable {
    val deserializedParameters = getArgs(function.valueParameters, serializedParameters, instance)
    val result = function.callSuspend(*deserializedParameters.toTypedArray())
    return serializeResult(function, result)
}

fun Route.rpc(controllerClass: KClass<out RpcController>) {
    val instance = controllerClass.createInstance()

    controllerClass.declaredMemberFunctions.forEach { function ->

        when (function.findAnnotation<Method>()?.type) {
            MethodType.GET -> get(function.name) {
                val serializedParameters = call.request.queryParameters.toMap().mapValues { it.value.toString() }
                val serializedResult = processRequest(function, serializedParameters, instance)
                call.respond(serializedResult)
            }
            MethodType.POST -> post(function.name) {
                val serializedParameters = Json { isLenient = true }.decodeFromString(
                    MapSerializer(String.serializer(), String.serializer()),
                    call.receiveText()
                )
                val serializedResult = processRequest(function, serializedParameters, instance)
                call.respond(serializedResult)
            }
            null -> error("Cannot find annotation")
        }
    }
}