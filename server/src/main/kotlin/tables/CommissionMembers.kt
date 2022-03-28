package tables

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CommissionMembers : IntIdTable() {
    val name = text("name")
    val status = text("status")
    val nominator = text("nominator") //Кем предложен в состав комиссии
}


class CommissionMember(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CommissionMember>(CommissionMembers)

    var name by CommissionMembers.name
    var status by CommissionMembers.status
    var nominator by CommissionMembers.nominator //Кем предложен в состав комиссии
}