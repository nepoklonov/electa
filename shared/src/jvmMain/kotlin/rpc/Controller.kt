package rpc

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException


@OptIn(InternalSerializationApi::class)
@Suppress("UNCHECKED_CAST")

fun Route.rpc(serviceClass: KClass<out RPCService>) {
    val instance = serviceClass.createInstance()

    suspend fun queryBody(function: KFunction<*>, call: ApplicationCall, queryParameters: Map<String, String>) {

        val args = mutableListOf<Any?>(instance)
        function.valueParameters.mapTo(args) { param ->
            Json { isLenient = true }.decodeFromString(
                param.type.jvmErasure.serializer(),
                queryParameters[param.name.toString()] ?: error("param is missing")
            )
        }

        val result = function.callSuspend(*args.toTypedArray())
        val serializedResult = if (function.returnType.arguments.isNotEmpty()) {
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
        call.respond(serializedResult)
    }

    serviceClass.declaredMemberFunctions.map { function ->
        if (function.name.startsWith("get")) {
            get(function.name) {
                val queryParameters = call.request.queryParameters.toMap().mapValues { it.value.toString() }
                queryBody(function, call, queryParameters)
            }
        } else {
            post(function.name) {
                val queryParameters = Json { isLenient = true }.decodeFromString(
                    MapSerializer(String.serializer(), String.serializer()),
                    call.receiveText()
                )
                queryBody(function, call, queryParameters)
            }
        }
    }
}