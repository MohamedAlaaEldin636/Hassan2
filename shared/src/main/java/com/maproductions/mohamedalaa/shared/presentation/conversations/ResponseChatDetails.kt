package com.maproductions.mohamedalaa.shared.presentation.conversations

import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging

data class ResponseChatDetails(
    var name: String?,
    var image: String?,
    var status: String?,
    var messages: MABasePaging<ItemConversation>?,
) {
    val statusOfOrder get() = ApiOrderStatus.values().firstOrNull { it.apiValue == status }

    val isStatusFinished get() = statusOfOrder == ApiOrderStatus.FINISHED
}
