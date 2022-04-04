import controllers.rpc.GetServerRPCTestController
import controllers.rpc.PostServerRPCTestController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import rpc.*
import test.RPCTestEnumArgument
import test.RPCTestSealedArgument
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

val postController = PostServerRPCTestController()
val getController = GetServerRPCTestController()

inline fun <reified T> testRPC(
    url: String,
    type: MethodType,
    body: Body = Body(),
    expected: T
) = withTestApplication(Application::test) {

    val method = if (type == MethodType.GET) Get else Post
    val fullURL = if (type == MethodType.GET) body.toGetQueryBy(url) else url

    with(handleRequest(method, fullURL) {
        addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
        if (!body.isEmpty) setBody(body.toString())
    }) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(expected, response.content?.let { json.decodeFromString(it) })
    }
}

class RpcTest {
    @Test
    fun `send two nums and receive their product by GET`() = testRPC(
        url = "/api/intProduct",
        body = build {
            set("arg1", intArg1)
            set("arg2", intArg2)
        },
        type = MethodType.GET,
        expected = getController.intProduct(intArg1, intArg2)
    )

    @Test
    fun `send two nums and receive their product by POST`() = testRPC(
        url = "/api/intProduct",
        type = MethodType.POST,
        body = build {
            set("arg1", intArg1)
            set("arg2", intArg2)
        },
        expected = postController.intProduct(intArg1, intArg2)
    )

    @Test
    fun `send two strings and receive their sum by GET`() = testRPC(
        url = "/api/strSum",
        body = build {
            set("arg1", strArg1)
            set("arg2", strArg2)
        },
        type = MethodType.GET,
        expected = getController.strSum(strArg1, strArg2)
    )

    @Test
    fun `send two strings and receive their sum by POST`() = testRPC(
        url = "/api/strSum",
        type = MethodType.POST,
        body = build {
            set("arg1", strArg1)
            set("arg2", strArg2)
        },
        expected = postController.strSum(strArg1, strArg2)
    )

    @Test
    fun `send two lists and receive their combination by GET`() = testRPC(
        url = "/api/listSum",
        body = build {
            set("arg1", listArg1)
            set("arg2", listArg2)
        },
        type = MethodType.GET,
        expected = getController.listSum(listArg1, listArg2)
    )

    @Test
    fun `send two lists and receive their combination by POST`() = testRPC(
        url = "/api/listSum",
        type = MethodType.POST,
        body = build {
            set("arg1", listArg1)
            set("arg2", listArg2)
        },
        expected = postController.listSum(listArg1, listArg2)
    )

    @Test
    fun `send two maps and receive their join by GET`() = testRPC(
        url = "/api/mapsJoin",
        body = build {
            set("arg1", mapArg1)
            set("arg2", mapArg2)
        },
        type = MethodType.GET,
        expected = getController.mapsJoin(mapArg1, mapArg2)
    )

    @Test
    fun `send two maps and receive their join by POST`() = testRPC(
        url = "/api/mapsJoin",
        type = MethodType.POST,
        body = build {
            set("arg1", mapArg1)
            set("arg2", mapArg2)
        },
        expected = postController.mapsJoin(mapArg1, mapArg2)
    )

    @Test
    fun `send two maps(Map, Map) and receive their sum by GET`() = testRPC(
        url = "/api/nestedMapsJoin",
        body = build {
            set("arg1", mmArg1)
            set("arg2", mmArg2)
        },
        type = MethodType.GET,
        expected = getController.nestedMapsJoin(mmArg1, mmArg2)
    )

    @Test
    fun `send two maps(Map, Map) and receive their sum by POST`() = testRPC(
        url = "/api/nestedMapsJoin",
        type = MethodType.POST,
        body = build {
            set("arg1", mmArg1)
            set("arg2", mmArg2)
        },
        expected = postController.nestedMapsJoin(mmArg1, mmArg2)
    )

    @Test
    fun `send two pairs and receive their sum by GET`() = testRPC(
        url = "/api/pairOfPair",
        body = build {
            set("arg1", pairArg1)
            set("arg2", pairArg2)
        },
        type = MethodType.GET,
        expected = getController.pairOfPair(pairArg1, pairArg2)
    )

    @Test
    fun `send two pairs and receive their sum by POST`() = testRPC(
        url = "/api/pairOfPair",
        type = MethodType.POST,
        body = build {
            set("arg1", pairArg1)
            set("arg2", pairArg2)
        },
        expected = postController.pairOfPair(pairArg1, pairArg2)
    )

    @Test
    fun `send two enum class instances and receive their sum by GET`() = testRPC(
        url = "/api/enumClassStr",
        body = build {
            set("arg1", RPCTestEnumArgument.ARG1)
            set("arg2", RPCTestEnumArgument.ARG2)
        },
        type = MethodType.GET,
        expected = getController.enumClassStr(RPCTestEnumArgument.ARG1, RPCTestEnumArgument.ARG2)
    )

    @Test
    fun `send two enum class instances and receive their sum by POST`() = testRPC(
        url = "/api/enumClassStr",
        type = MethodType.POST,
        body = build {
            set("arg1", RPCTestEnumArgument.ARG1)
            set("arg2", RPCTestEnumArgument.ARG2)
        },
        expected = postController.enumClassStr(RPCTestEnumArgument.ARG1, RPCTestEnumArgument.ARG2)
    )

    private val arg: RPCTestSealedArgument = RPCTestSealedArgument.Const(1)

    @Test
    fun `send one sealed class instance and receive its num by GET`() = testRPC(
        url = "/api/sealedClassNum",
        body = build {
            set("arg1", arg)
        },
        type = MethodType.GET,
        expected = getController.sealedClassNum(arg)
    )

    @Test
    fun `send one sealed class instance and receive its num by POST`() = testRPC(
        url = "/api/sealedClassNum",
        type = MethodType.POST,
        body = build {
            set("arg1", arg)
        },
        expected = postController.sealedClassNum(arg)
    )
}