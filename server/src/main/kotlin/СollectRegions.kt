import dto.RegionDto
import io.ktor.client.*
import io.ktor.client.request.*
import org.jsoup.nodes.Document


suspend fun collectRegions(client: HttpClient): Set<RegionDto> {
    client.use {
        val feed = it.get<Document>("http://cikrf.ru/izbiratelnym-komissiyam/sites/")
        val regionsSelect = feed.select("div.cec-select")
        if (regionsSelect.isEmpty()) {
            error("no regions found")
        }
        return regionsSelect.select("option").mapNotNull { region ->
            val regex = "http://(?:www\\.)?(.*)\\.izbirkom\\.ru".toRegex()
            val link = region.attr("value")
            val codename = regex.find(link)?.groupValues?.get(1) ?: return@mapNotNull null
            RegionDto(
                codename = codename,
                title = region.text()
            )
        }.toSet()
    }
}