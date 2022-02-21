import com.tfowl.ktor.client.features.JsoupFeature
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.html.*
import io.ktor.routing.*
import kotlinx.coroutines.launch
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.script
import org.jetbrains.exposed.sql.SchemaUtils

@Suppress("unused")
fun Application.module() {
    val client = HttpClient() {
        install(JsoupFeature)
    }
    launch {
        // collectRegions(client)
    }

    database {
        SchemaUtils.create(Regions)
    }

    routing {
        get("{...}") {
            call.respondHtml {
                body {
                    div {
                        id = "react-app"
                        +"Waiting"
                    }
                    script(src = "/client.js") { }
                }
            }
        }
    }
}