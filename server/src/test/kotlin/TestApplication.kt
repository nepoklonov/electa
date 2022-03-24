import io.ktor.application.*
import io.ktor.routing.*
import rpc.rpc

@Suppress("unused")
fun Application.test() {
    routing {
        route("/api") {
            rpc(PostServerController::class)
            rpc(GetServerController::class)
        }
    }
}