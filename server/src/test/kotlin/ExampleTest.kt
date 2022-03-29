import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import rpc.MethodType
import rpc.json
import kotlin.test.*


const val intArg1: Int = 5
const val intArg2: Int = 5

const val strArg1: String = "222"
const val strArg2: String = "555"

val listArg1: MutableList<Int> = mutableListOf(1, 2, 3, 4, 5)
val listArg2: MutableList<String> = mutableListOf("a", "b", "c", "d", "e")

val mapArg1: Map<Int, String> = mapOf(1 to "one", 2 to "two")
val mapArg2: Map<String, Int> = mapOf("one" to 1, "two" to 2)

val mmArg1: Map<Map<Int, String>, Map<String, Int>> = mapOf(mapArg1 to mapArg2)
val mmArg2: Map<Map<String, Int>, Map<Int, String>> = mapOf(mapArg2 to mapArg1)

val pairArg1: Pair<Int, Int> = 1 to 2
val pairArg2: Pair<Char, Char> = 'a' to 'b'

val postController = PostServerController()
val getController = GetServerController()

fun buildPost(block: Body.() -> Unit): Map<String, String> {
    return Body().apply(block).map
}

fun buildGet(block: Body.() -> Unit): String{
    var request = "?"
    Body().apply(block).map.forEach {
        request += "${it.key}=${it.value}&"
    }
    return request.dropLast(1)
}

class Body {
    val map: MutableMap<String, String> = mutableMapOf()
    
    inline fun <reified T> set(name: String, value: T) {
        map[name] = json.encodeToString(value)
    }
}

inline fun <reified T> testRpc(url: String, type: MethodType, body: Map<String, String> = mapOf(), expected: T) =
    withTestApplication(Application::test) {
        with(handleRequest(if(type == MethodType.GET) Get else Post, url) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            if (body != {}) setBody(json.encodeToString(body))
        })
        {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expected, response.content?.let { json.decodeFromString(it) })
        }
    }

class RpcTest {

    @Test
    fun `send two nums and receive their product by GET`() = testRpc(
        url = "/api/intProduct" + buildGet{
            set("arg1", intArg1)
            set("arg2", intArg2)
        },
        type = MethodType.GET,
        expected = getController.intProduct(intArg1, intArg2)
    )

    @Test
    fun `send two nums and receive their product by POST`() = testRpc(
        url = "/api/intProduct",
        type = MethodType.POST,
        body = buildPost {
            set("arg1", intArg1)
            set("arg2", intArg2)
        },
        expected = postController.intProduct(intArg1, intArg2)
    )

    @Test
    fun `send two strings and receive their sum by GET`() = testRpc(
        url = "/api/strSum" + buildGet{
            set("arg1", strArg1)
            set("arg2", strArg2)
        },
        type = MethodType.GET,
        expected = getController.strSum(strArg1, strArg2)
    )

    @Test
    fun `send two strings and receive their sum by POST`() = testRpc(
        url = "/api/strSum",
        type = MethodType.POST,
        body = buildPost {
            set("arg1", strArg1)
            set("arg2", strArg2)
        },
        expected = postController.strSum(strArg1, strArg2)
    )

    @Test
    fun `send two lists and receive their combination by GET`() = testRpc(
        url = "/api/listSum" + buildGet{
            set("arg1", listArg1)
            set("arg2", listArg2)
        },
        type = MethodType.GET,
        expected = getController.listSum(listArg1, listArg2)
    )

    @Test
    fun `send two lists and receive their combination by POST`() = testRpc(
        url = "/api/listSum",
        type = MethodType.POST,
        body = buildPost {
            set("arg1", listArg1)
            set("arg2", listArg2)
        },
        expected = postController.listSum(listArg1, listArg2)
    )

    @Test
    fun `send two maps and receive their join by GET`() = testRpc(
        url = "/api/mapJoin" + buildGet{
            set("arg1", mapArg1)
            set("arg2", mapArg2)
        },
        type = MethodType.GET,
        expected = getController.mapJoin(mapArg1, mapArg2)
    )

    @Test
    fun `send two maps and receive their join by POST`() = testRpc(
        url = "/api/mapJoin",
        type = MethodType.POST,
        body = buildPost {
            set("arg1", mapArg1)
            set("arg2", mapArg2)
        },
        expected = postController.mapJoin(mapArg1, mapArg2)
    )

    @Test
    fun `send two maps(Map, Map) and receive their sum by GET`() = testRpc(
        url = "/api/mmJoin" + buildGet{
            set("arg1", mmArg1)
            set("arg2", mmArg2)
        },
        type = MethodType.GET,
        expected = getController.mmJoin(mmArg1, mmArg2)
    )

    @Test
    fun `send two maps(Map, Map) and receive their sum by POST`() = testRpc(
        url = "/api/mmJoin",
        type = MethodType.POST,
        body = buildPost {
            set("arg1", mmArg1)
            set("arg2", mmArg2)
        },
        expected = postController.mmJoin(mmArg1, mmArg2)
    )

    @Test
    fun `send two pairs and receive their sum by GET`() = testRpc(
        url = "/api/pairOfPair" + buildGet{
            set("arg1", pairArg1)
            set("arg2", pairArg2)
        },
        type = MethodType.GET,
        expected = getController.pairOfPair(pairArg1, pairArg2)
    )

    @Test
    fun `send two pairs and receive their sum by POST`() = testRpc(
        url = "/api/pairOfPair",
        type = MethodType.POST,
        body = buildPost {
            set("arg1", pairArg1)
            set("arg2", pairArg2)
        },
        expected = postController.pairOfPair(pairArg1, pairArg2)
    )

    @Test
    fun `send two enum class instances and receive their sum by GET`() = testRpc(
        url = "/api/enumClassStr" + buildGet{
            set("arg1", EnumArgumentsForRPCTests.ARG1)
            set("arg2", EnumArgumentsForRPCTests.ARG2)
        },
        type = MethodType.GET,
        expected = getController.enumClassStr(EnumArgumentsForRPCTests.ARG1, EnumArgumentsForRPCTests.ARG2)
    )

    @Test
    fun `send two enum class instances and receive their sum by POST`() = testRpc(
        url = "/api/enumClassStr",
        type = MethodType.POST,
        body = buildPost {
            set("arg1", EnumArgumentsForRPCTests.ARG1)
            set("arg2", EnumArgumentsForRPCTests.ARG2)
        },
        expected = postController.enumClassStr(EnumArgumentsForRPCTests.ARG1, EnumArgumentsForRPCTests.ARG2)
    )

    private val arg: SealedArgumentsForRPCTests = SealedArgumentsForRPCTests.Const(1)

    @Test
    fun `send one sealed class instance and receive its num by GET`() = testRpc(
        url = "/api/sealedClassNum" + buildGet{
            set("arg1", arg)
        },
        type = MethodType.GET,
        expected = getController.sealedClassNum(arg)
    )

    @Test
    fun `send one sealed class instance and receive its num by POST`() = testRpc(
        url = "/api/sealedClassNum",
        type = MethodType.POST,
        body = buildPost {
            set("arg1", arg)
        },
        expected = postController.sealedClassNum(arg)
    )
}