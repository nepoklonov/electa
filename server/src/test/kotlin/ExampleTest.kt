import io.ktor.application.*
import io.ktor.http.*
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

val listArg1: MutableList<Int> = mutableListOf(1,2,3,4,5)
val listArg2: MutableList<String> = mutableListOf("a","b","c","d","e")

val mapArg1: Map<Int, String> = mapOf(1 to "one", 2 to "two")
val mapArg2: Map<String, Int> = mapOf("three" to 3, "four" to 4)

val mmArg1: Map<Map<Int, String>, Map<String, Int>> = mapOf(mapArg1 to mapArg2)
val mmArg2: Map<Map<String, Int>, Map<Int, String>> = mapOf(mapArg2 to mapArg1)

val pairArg1: Pair<Int, Int> = Pair(1, 2)
val pairArg2: Pair<Char, Char> = Pair('a', 'b')

enum class EnumArguments(val par: Int){
    ARG1(123),
    ARG2(321)
}

@Serializable
sealed class SealedArguments {
    @Serializable
    class Const(val num: Int): SealedArguments()
}

val testController = TestController()

fun builder(block: Body.() -> Unit): Map<String, String> {
    return Body().apply(block).map
}

class Body {
    val map: MutableMap<String, String> = mutableMapOf()

    inline fun <reified T> let(name: String, value: T) {
        map[name] = json.encodeToString(value)

    }
}

class RpcTest {

    @Test
    fun `send two nums and receive their product by GET`() {
        withTestApplication(Application::test) {
            handleRequest(HttpMethod.Get, "/api/getInt?arg1=$intArg1&arg2=$intArg2").apply {
                val ans = testController.getInt(intArg1, intArg2)
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ans.toString(), response.content)
            }
        }
    }

    @Test
    fun `send two nums and receive their product by POST`() = withTestApplication(Application::test) {
        with(handleRequest(HttpMethod.Post, "/api/postInt") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            val x = builder{
                let("arg1", intArg1)
                let("arg2", intArg2)
            }
            setBody(json.encodeToString(x))
        })
        {
            val ans = testController.postInt(intArg1, intArg2)
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(ans.toString(), response.content)
        }
    }

    @Test
    fun `send two strings and receive their sum by GET`() {
        withTestApplication(Application::test) {
            handleRequest(HttpMethod.Get, "/api/getStr?arg1=${json.encodeToString(strArg1)}&arg2=${json.encodeToString(strArg2)}").apply {
                val ans = testController.getStr(strArg1, strArg2)
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ans, response.content?.let { json.decodeFromString(it) })
            }
        }
    }

    @Test
    fun `send two strings and receive their sum by POST`() = withTestApplication(Application::test) {
        with(handleRequest(HttpMethod.Post, "/api/postStr") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            val x = builder{
                let("arg1", strArg1)
                let("arg2", strArg2)
            }
            setBody(json.encodeToString(x))
        })
        {
            val ans = testController.postStr(strArg1, strArg2)
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(ans, response.content?.let { json.decodeFromString(it) })
        }
    }

    @Test
    fun `send two lists and receive their combination by GET`() {
        withTestApplication(Application::test) {
            handleRequest(HttpMethod.Get, "/api/getList?arg1=$listArg1&arg2=${json.encodeToString(listArg2)}").apply {
                val ans = testController.getList(listArg1, listArg2).also{println(it)}
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ans, response.content?.let { json.decodeFromString(it) })
            }
        }
    }

    @Test
    fun `send two lists and receive their combination by POST`() = withTestApplication(Application::test) {
        with(handleRequest(HttpMethod.Post, "/api/postList") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            val x = builder{
                let("arg1", listArg1)
                let("arg2", listArg2)
            }
            setBody(json.encodeToString(x))
        })
        {
            val ans = testController.postList(listArg1, listArg2)
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(ans, response.content?.let { json.decodeFromString(it) })
        }
    }

    @Test
    fun `send two maps and receive their sum by GET`() {
        withTestApplication(Application::test) {
            handleRequest(HttpMethod.Get, "/api/getMap?arg1=${json.encodeToString(mapArg1)}&arg2=${json.encodeToString(mapArg2)}").apply {
                val ans = testController.getMap(mapArg1, mapArg2).also{println(it)}
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ans, response.content?.let { json.decodeFromString(it) })
            }
        }
    }

    @Test
    fun `send two maps and receive their sum by POST`() = withTestApplication(Application::test) {
        with(handleRequest(HttpMethod.Post, "/api/postMap") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            val x = builder{
                let("arg1", mapArg1)
                let("arg2", mapArg2)
            }
            setBody(json.encodeToString(x))
        })
        {
            val ans = testController.postMap(mapArg1, mapArg2)
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(ans, response.content?.let { json.decodeFromString(it) })
        }
    }

    @Test
    fun `send two maps(Map, Map) and receive their sum by GET`() {
        withTestApplication(Application::test) {
            handleRequest(HttpMethod.Get, "/api/getMM?arg1=${json.encodeToString(mmArg1)}&arg2=${json.encodeToString(mmArg2)}").apply {
                val ans = testController.getMM(mmArg1, mmArg2).also{println(it)}
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ans, response.content?.let { json.decodeFromString(it) })
            }
        }
    }

    @Test
    fun `send two maps(Map, Map) and receive their sum by POST`() = withTestApplication(Application::test) {
        with(handleRequest(HttpMethod.Post, "/api/postMM") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            val x = builder{
                let("arg1", mmArg1)
                let("arg2", mmArg2)
            }
            setBody(json.encodeToString(x))
        })
        {
            val ans = testController.postMM(mmArg1, mmArg2)
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(ans, response.content?.let { json.decodeFromString(it) })
        }
    }

    @Test
    fun `send two pairs and receive their sum by GET`() {
        withTestApplication(Application::test) {
            handleRequest(HttpMethod.Get, "/api/getPair?arg1=${json.encodeToString(pairArg1)}&arg2=${json.encodeToString(pairArg2)}").apply {
                val ans = testController.getPair(pairArg1, pairArg2).also{println(it)}
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ans, response.content?.let { json.decodeFromString(it) })
            }
        }
    }

    @Test
    fun `send two pairs and receive their sum by POST`() = withTestApplication(Application::test) {
        with(handleRequest(HttpMethod.Post, "/api/postPair") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            val x = builder{
                let("arg1", pairArg1)
                let("arg2", pairArg2)
            }
            setBody(json.encodeToString(x))
        })
        {
            val ans = testController.postPair(pairArg1, pairArg2)
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(ans, response.content?.let { json.decodeFromString(it) })
        }
    }

    @Test
    fun `send two enum class instances and receive their sum by GET`() {
        withTestApplication(Application::test) {
            handleRequest(HttpMethod.Get, "/api/getEnumClass?arg1=${json.encodeToString(EnumArguments.ARG1)}&arg2=${json.encodeToString(EnumArguments.ARG2)}").apply {
                val ans = testController.getEnumClass(EnumArguments.ARG1, EnumArguments.ARG2)
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ans, response.content?.let { json.decodeFromString(it) })
            }
        }
    }

    @Test
    fun `send two enum class instances and receive their sum by POST`() = withTestApplication(Application::test) {
        with(handleRequest(HttpMethod.Post, "/api/postEnumClass") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            val x = builder{
                let("arg1", EnumArguments.ARG1)
                let("arg2", EnumArguments.ARG2)
            }
            setBody(json.encodeToString(x))
        })
        {
            val ans = testController.postEnumClass(EnumArguments.ARG1, EnumArguments.ARG2)
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(ans, response.content?.let { json.decodeFromString(it) })
        }
    }

    @Test
    fun `send one sealed class instance and receive him num by GET`() {
        withTestApplication(Application::test) {
            val arg: SealedArguments = SealedArguments.Const(1)
            handleRequest(HttpMethod.Get, "/api/getSealedClass?arg1=${json.encodeToString(arg)}").apply {
                val ans = testController.getSealedClass(SealedArguments.Const(1))
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ans, response.content?.let { json.decodeFromString(it) })
            }
        }
    }

    @Test
    fun `send one sealed class instance and receive him num by POST`() = withTestApplication(Application::test) {
        with(handleRequest(HttpMethod.Post, "/api/postSealedClass") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            val arg: SealedArguments = SealedArguments.Const(1)
            val x = builder{
                let("arg1", arg)
            }
            setBody(json.encodeToString(x))
        })
        {
            val ans = testController.postSealedClass(SealedArguments.Const(1))
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(ans, response.content?.let { json.decodeFromString(it) })
        }
    }
}