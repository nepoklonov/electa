import dto.RegionDto
import tables.Region


fun fillRegions(regions: Set<RegionDto>) {
    transactionWithLogger {
        regions.forEach { region ->
            Region.new {
                codename = region.codename
                title = region.title
            }
        }
    }
}