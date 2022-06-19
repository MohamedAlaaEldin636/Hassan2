package com.maproductions.mohamedalaa.shared.presentation.conversations

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.AccountType

data class AccountInChat(
    var id: Int,
    var name: String,
    @SerializedName("image") var imageUrl: String,
    @SerializedName("account_type") var typeOfAccount: String
) {
    val accountType get() = AccountType.values().first { it.apiValue == typeOfAccount }
}
