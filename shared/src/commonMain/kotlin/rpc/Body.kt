package rpc

import kotlinx.serialization.encodeToString

class Body {
    val map: MutableMap<String, String> = mutableMapOf()

    inline fun <reified T> set(name: String, value: T) {
        map[name] = json.encodeToString(value)
    }

    override fun toString(): String = json.encodeToString(map)
}

val Body.isEmpty get() = map.isEmpty()

fun build(block: Body.() -> Unit): Body {
    return Body().apply(block)
}

fun Body.toGetQueryBy(url: String) = map.entries.joinToString(
    separator = "&", prefix = "$url?"
) { (key, value) -> "$key=$value" }
