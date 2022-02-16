import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class RpcTest {
    @Test
    fun testRpc() {
        withTestApplication(Application::test) {
            handleRequest(HttpMethod.Get, "/api/getExampleFun?arg=5").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("25", response.content)
            }
        }
    }
}