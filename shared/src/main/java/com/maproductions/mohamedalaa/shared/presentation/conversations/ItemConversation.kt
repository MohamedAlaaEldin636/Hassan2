package com.maproductions.mohamedalaa.shared.presentation.conversations

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.presentation.myAccount.ItemFile

data class ItemConversation(
    var id: Int,
    var message: String?,
    var time: String?,
    @SerializedName("image") var imageUrl: String?,
    var user: AccountInChat,
    @SerializedName("order_id") var orderId: Int?,
    @SerializedName("status") var orderStatus: String?,
) {
    val statusOfOrder get() = ApiOrderStatus.values().firstOrNull { it.apiValue == orderStatus }

    val isStatusFinished get() = statusOfOrder == ApiOrderStatus.FINISHED
}
