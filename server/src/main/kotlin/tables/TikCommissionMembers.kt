package tables

import org.jetbrains.exposed.sql.Table

object TikCommissionMembers : Table() {
    private val tik = reference("tik", Tiks)
    private val commissionMember = reference("commissionMember", CommissionMembers)
    override val primaryKey = PrimaryKey(tik, commissionMember)
}