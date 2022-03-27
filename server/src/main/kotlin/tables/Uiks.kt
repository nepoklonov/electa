package tables

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Uiks : IntIdTable() {
    val title = text("title")
    val vrn = integer("vrn")
    val num = integer("num")

    val regionId = integer("regionId")

    val commissionAddress = text("commissionAddress") //Адрес комиссии
    val commissionLatitude = float("commissionLatitude")// широта
    val commissionLongitude = float("commissionLongitude") // долгота
    val phone = text("phone")
    val fax = text("fax")
    val email = text("email")
    val endDate = text("endDate") //Срок окончания полномочий
    val extensionDate = text("extensionDate") //Продление срока полномочий
    val pollingStationAddress = text("pollingStationAddress") //Адрес помещения для голосования
    val pollingStationLatitude = float("pollingStationLatitude")// широта
    val pollingStationLongitude = float("pollingStationLongitude") // долгота
    val pollingStationType = text("pollingStationType") //Тип помещения для голосования
    //TODO: commissionMembers -- Члены избирательной комиссии с правом решающего голоса
}


class Uik(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Uik>(Uiks)

    var title by Uiks.title
    var vrn by Uiks.vrn
    var num by Uiks.num

    var regionId by Uiks.regionId

    var commissionAddress by Uiks.commissionAddress //Адрес комиссии
    var commissionLatitude by Uiks.commissionLatitude// широта
    var commissionLongitude by Uiks.commissionLongitude// долгота
    var phone by Uiks.phone
    var fax by Uiks.fax
    var email by Uiks.email
    var endDate by Uiks.endDate //Срок окончания полномочий
    var extensionDate by Uiks.extensionDate //Продление срока полномочий
    var pollingStationAddress by Uiks.pollingStationAddress //Адрес помещения для голосования
    var pollingStationLatitude by Uiks.pollingStationLatitude// широта
    var pollingStationLongitude by Uiks.pollingStationLongitude// долгота
    var pollingStationType by Uiks.pollingStationType //Тип помещения для голосования
    //TODO: commissionMembers -- Члены избирательной комиссии с правом решающего голоса
}