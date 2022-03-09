import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import org.w3c.fetch.RequestInit

class Transport {
    suspend inline fun <reified T> send(requestType: String, url: String, vararg arg: Pair<String, Any>) : T {
        val args = Json.encodeToString(arg.toMap().mapValues { Json.encodeToString(it.value) })
        val response = window.fetch(url, RequestInit(method = requestType, body = args)).await().text().await()
        return Json.decodeFromString<T>(response)
    }

    suspend inline fun <reified T> get(url: String, vararg arg: Pair<String, Any>) : T {
        return send<T>("get", url, *arg)
    }

    suspend inline fun <reified T> post(url: String, vararg arg: Pair<String, Any>) : T {
        return send<T>("post", url, *arg)
    }
}
