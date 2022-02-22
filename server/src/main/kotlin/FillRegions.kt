import org.jetbrains.exposed.sql.insert

fun fillRegions(regions: Set<Region>) {
    regions.forEach { region ->
        database {
            Regions.insert {
                it[codename] = region.codename
                it[title] = region.title
            }
        }
    }
}