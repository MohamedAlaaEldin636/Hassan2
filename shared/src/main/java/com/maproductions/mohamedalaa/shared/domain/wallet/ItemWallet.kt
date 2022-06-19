package com.maproductions.mohamedalaa.shared.domain.wallet

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.R
import timber.log.Timber

data class ItemWallet(
    @SerializedName("wallet_type") var valueType: String,
    var amount: Float,
    var date: String?,
    @SerializedName("wallet_reason") var walletReason: String?, // "wallet_reason":"app-percentage"
) {

    enum class Type(val apiValue: String) {
        MINUS("minus"),
        PLUS("add"),
    }

    enum class Reason(val apiValue: String) {
        CANCELLED_ORDER("cancelled-order"),
        APP_PERCENTAGE("app-percentage"),
        WALLET_ADDITION("wallet-addition"),
    }

    val reason get() = Reason.values().firstOrNull { it.apiValue == walletReason }

    val type get() = Type.values().first { it.apiValue == valueType }

    val isMinus get() = type == Type.MINUS

    fun getReadableReason(context: Context): String = context.run {
        when (reason) {
            Reason.CANCELLED_ORDER -> getString(R.string.cancel_order)
            Reason.APP_PERCENTAGE -> getString(R.string.app_took_percentage)
            Reason.WALLET_ADDITION -> getString(R.string.paid_more_money_in_order)
            null -> getString(R.string.unknown_2)
        }
    }

}
