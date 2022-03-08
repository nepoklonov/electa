package tables

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Regions : IntIdTable() {
    val codename = text("codename")
    val title = text("title")
}


class Region(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Region>(Regions)

    var codename by Regions.codename
    var title by Regions.title
}