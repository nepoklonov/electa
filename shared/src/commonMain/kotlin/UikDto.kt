data class UikDto(
    val id: Int,
    val title: String,
    val commissionAddress: String, //Адрес комиссии
    val commissionLatitude: Float, // широта
    val commissionLongitude: Float, // долгота
    val phone: String,
    val fax: String,
    val email: String,
    val endDate: String, //Срок окончания полномочий
    val extensionDate: String, //Продление срока полномочий
    val pollingStationAddress: String, //Адрес помещения для голосования
    val pollingStationLatitude: Float, // широта
    val pollingStationLongitude: Float, // долгота
    val pollingStationType: String, //Тип помещения для голосования
    //TODO: commissionMembers -- Члены избирательной комиссии с правом решающего голоса
)
