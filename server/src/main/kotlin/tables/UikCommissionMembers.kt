package tables

import org.jetbrains.exposed.sql.Table

object UikCommissionMembers : Table() {
    private val uik = reference("uik", Uiks)
    private val commissionMember = reference("commissionMember", CommissionMembers)
    override val primaryKey = PrimaryKey(uik, commissionMember)
}