import controllers.rpc.GetServerRPCTestController
import controllers.rpc.PostServerRPCTestController
import io.ktor.application.*
import io.ktor.routing.*
import rpc.rpc

@Suppress("unused")
fun Application.test() {
    routing {
        route("/api") {
            rpc(PostServerRPCTestController::class)
            rpc(GetServerRPCTestController::class)
        }
    }
}