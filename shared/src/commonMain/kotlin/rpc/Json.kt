package rpc

import kotlinx.serialization.json.Json

val json = Json {
    allowStructuredMapKeys = true
    isLenient = true
}