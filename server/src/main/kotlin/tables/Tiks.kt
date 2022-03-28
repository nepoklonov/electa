package tables

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import tables.Uik.Companion.referrersOn

object Tiks : IntIdTable() {
    val title = text("title")
    val vrn = integer("vrn")

    val region = reference("region", Regions)

    val address = text("address")
    val latitude = float("latitude")// широта
    val longitude = float("longitude") // долгота
    val phone = text("phone")
    val fax = text("fax")
    val email = text("email")
    val endDate = text("endDate") //Срок окончания полномочий
    val extensionDate = text("extensionDate") //Продление срока полномочий
}


class Tik(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Tik>(Tiks)

    var title by Tiks.title
    var vrn by Tiks.vrn

    var region by Region referencedOn Tiks.region

    var address by Tiks.address
    var latitude by Tiks.latitude// широта
    var longitude by Tiks.longitude// долгота
    var phone by Tiks.phone
    var fax by Tiks.fax
    var email by Tiks.email
    var endDate by Tiks.endDate //Срок окончания полномочий
    var extensionDate by Tiks.extensionDate //Продление срока полномочий

    var commissionMember by CommissionMember via TikCommissionMembers //Члены избирательной комиссии с правом решающего голоса
}