import io.ktor.application.*
import io.ktor.routing.*

@Suppress("unused")
fun Application.test() {
    routing {
        route("/api") {
            rpc(TestController::class)
        }
    }
}