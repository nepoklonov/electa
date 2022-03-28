import io.ktor.client.*
import io.ktor.client.request.*
import org.jsoup.nodes.Document

suspend fun collectRegions(client: HttpClient): MutableSet<RegionDto> {
    client.use {

        val feed = it.get<Document>("http://cikrf.ru/izbiratelnym-komissiyam/sites/")

        val regions = mutableSetOf<RegionDto>()

        feed.select("option").forEach { region ->
            val codename =
                region.attr("value").substringAfter("http://").substringAfter("www.").substringBefore(".izbirkom.ru")
            val title = region.text()

            if (codename != "" && title != "") {
                regions.add(
                    RegionDto(
                        codename = codename,
                        title = title,
                        code = null
                    )
                )
            }
        }

        return regions
    }
}