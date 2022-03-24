import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.server.testing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
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

val pairArg1: Pair<Int, Int> = Pair(1, 2)
val pairArg2: Pair<Char, Char> = Pair('a', 'b')

enum class EnumArguments(val par: Int) {
    ARG1(123),
    ARG2(321)
}

@Serializable
sealed class SealedArguments {
    @Serializable
    class Const(val num: Int) : SealedArguments()
}

val postController = PostServerController()
val getController = GetServerController()

fun buildPost(block: Body.() -> Unit): Map<String, String> {
    return Body().apply(block).map
}

fun buildGet(block: Body.() -> Unit): String{
    return Body().apply(block).getUrl.dropLast(1)
}

class Body {
    val map: MutableMap<String, String> = mutableMapOf()
    var getUrl: String = ""
    
    inline fun <reified T> setPost(name: String, value: T) {
        map[name] = json.encodeToString(value)
    }
    inline fun <reified T> setGet(name: String, value: T) {
        getUrl += "$name=${json.encodeToString(value)}&"
    }
}

inline fun <reified T> testRpc(url: String, type: HttpMethod, body: Map<String, String>? = null, expected: T) =
    withTestApplication(Application::test) {
        with(handleRequest(type, url) {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            if (body != null) setBody(json.encodeToString(body))
        })
        {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(expected, response.content?.let { json.decodeFromString(it) })
        }
    }

class RpcTest {

    @Test
    fun `send two nums and receive their product by GET`() = testRpc(
        url = "/api/intProduct?" + buildGet{
            setGet("arg1", intArg1)
            setGet("arg2", intArg2)
        },
        type = Get,
        expected = getController.intProduct(intArg1, intArg2)
    )

    @Test
    fun `send two nums and receive their product by POST`() = testRpc(
        url = "/api/intProduct",
        type = Post,
        body = buildPost {
            setPost("arg1", intArg1)
            setPost("arg2", intArg2)
        },
        expected = postController.intProduct(intArg1, intArg2)
    )

    @Test
    fun `send two strings and receive their sum by GET`() = testRpc(
        url = "/api/strSum?" + buildGet{
            setGet("arg1", strArg1)
            setGet("arg2", strArg2)
        },
        type = Get,
        expected = getController.strSum(strArg1, strArg2)
    )

    @Test
    fun `send two strings and receive their sum by POST`() = testRpc(
        url = "/api/strSum",
        type = Post,
        body = buildPost {
            setPost("arg1", strArg1)
            setPost("arg2", strArg2)
        },
        expected = postController.strSum(strArg1, strArg2)
    )

    @Test
    fun `send two lists and receive their combination by GET`() = testRpc(
        url = "/api/listSum?" + buildGet{
            setGet("arg1", listArg1)
            setGet("arg2", listArg2)
        },
        type = Get,
        expected = getController.listSum(listArg1, listArg2)
    )

    @Test
    fun `send two lists and receive their combination by POST`() = testRpc(
        url = "/api/listSum",
        type = Post,
        body = buildPost {
            setPost("arg1", listArg1)
            setPost("arg2", listArg2)
        },
        expected = postController.listSum(listArg1, listArg2)
    )

    @Test
    fun `send two maps and receive their join by GET`() = testRpc(
        url = "/api/mapJoin?" + buildGet{
            setGet("arg1", mapArg1)
            setGet("arg2", mapArg2)
        },
        type = Get,
        expected = getController.mapJoin(mapArg1, mapArg2)
    )

    @Test
    fun `send two maps and receive their join by POST`() = testRpc(
        url = "/api/mapJoin",
        type = Post,
        body = buildPost {
            setPost("arg1", mapArg1)
            setPost("arg2", mapArg2)
        },
        expected = postController.mapJoin(mapArg1, mapArg2)
    )

    @Test
    fun `send two maps(Map, Map) and receive their sum by GET`() = testRpc(
        url = "/api/mmJoin?" + buildGet{
            setGet("arg1", mmArg1)
            setGet("arg2", mmArg2)
        },
        type = Get,
        expected = getController.mmJoin(mmArg1, mmArg2)
    )

    @Test
    fun `send two maps(Map, Map) and receive their sum by POST`() = testRpc(
        url = "/api/mmJoin",
        type = Post,
        body = buildPost {
            setPost("arg1", mmArg1)
            setPost("arg2", mmArg2)
        },
        expected = postController.mmJoin(mmArg1, mmArg2)
    )

    @Test
    fun `send two pairs and receive their sum by GET`() = testRpc(
        url = "/api/pairOfPair?" + buildGet{
            setGet("arg1", pairArg1)
            setGet("arg2", pairArg2)
        },
        type = Get,
        expected = getController.pairOfPair(pairArg1, pairArg2)
    )

    @Test
    fun `send two pairs and receive their sum by POST`() = testRpc(
        url = "/api/pairOfPair",
        type = Post,
        body = buildPost {
            setPost("arg1", pairArg1)
            setPost("arg2", pairArg2)
        },
        expected = postController.pairOfPair(pairArg1, pairArg2)
    )

    @Test
    fun `send two enum class instances and receive their sum by GET`() = testRpc(
        url = "/api/enumClassStr?" + buildGet {
            setGet("arg1", EnumArguments.ARG1)
            setGet("arg2", EnumArguments.ARG2)
        },
        type = Get,
        expected = getController.enumClassStr(EnumArguments.ARG1, EnumArguments.ARG2)
    )

    @Test
    fun `send two enum class instances and receive their sum by POST`() = testRpc(
        url = "/api/enumClassStr",
        type = Post,
        body = buildPost {
            setPost("arg1", EnumArguments.ARG1)
            setPost("arg2", EnumArguments.ARG2)
        },
        expected = postController.enumClassStr(EnumArguments.ARG1, EnumArguments.ARG2)
    )

    private val arg: SealedArguments = SealedArguments.Const(1)

    @Test
    fun `send one sealed class instance and receive him num by GET`() = testRpc(
        url = "/api/sealedClassNum?" + buildGet {
            setGet("arg1", arg)
        },
        type = Get,
        expected = getController.sealedClassNum(arg)
    )

    @Test
    fun `send one sealed class instance and receive him num by POST`() = testRpc(
        url = "/api/sealedClassNum",
        type = Post,
        body = buildPost {
            setPost("arg1", arg)
        },
        expected = postController.sealedClassNum(arg)
    )
}