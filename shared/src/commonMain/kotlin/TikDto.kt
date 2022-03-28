data class TikDto(
    val id: Int,
    val title: String,
    val address: String,
    val latitude: Float, // широта
    val longitude: Float, // долгота
    val phone: String,
    val fax: String,
    val email: String,
    val endDate: String, //Срок окончания полномочий
    val extensionDate: String, //Продление срока полномочий
    val commissionMembers: List<CommissionMemberDto>, //Члены избирательной комиссии с правом решающего голоса
)
