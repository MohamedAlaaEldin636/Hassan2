package com.maproductions.mohamedalaa.shared.domain.wallet

import com.google.gson.annotations.SerializedName
import timber.log.Timber

data class ItemWallet(
    @SerializedName("wallet_type") var valueType: String,
    var amount: Float,
    var date: String?
) {

    enum class Type(val apiValue: String) {
        MINUS("minus"),
        PLUS("add"),
    }

    val type get() = Type.values().first { it.apiValue == valueType }

    val isMinus get() = type == Type.MINUS

}
