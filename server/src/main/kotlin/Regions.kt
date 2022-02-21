import org.jetbrains.exposed.dao.id.IntIdTable

object Regions : IntIdTable() {
    val codename = text("codename")
    val title = text("title")
}