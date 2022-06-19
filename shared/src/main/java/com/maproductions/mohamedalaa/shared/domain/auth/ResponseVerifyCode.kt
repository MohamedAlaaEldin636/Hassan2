package com.maproductions.mohamedalaa.shared.domain.auth

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.AccountType
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory

/**
 * @param verified `1` means true.
 */
data class ResponseVerifyCode(
    var id: Int?,
    @SerializedName("account_type") var typeOfAccount: String?,
    var verified: Int?,

    var name: String?,
    var email: String?,

    var phone: String?,

    var latitude: String?,
    var longitude: String?,
    var address: String?,

    @SerializedName("token") var apiToken: String?,

    @SerializedName("image") var imageUrl: String?,

    @SerializedName("orders_flag") var ordersFlag: Int?,

    var services: List<ServiceInCategory>? = null,

    var category: String? = null
) {

    val stopReceivingOrders get() = ordersFlag == 1

    val isVerified get() = verified == 1

    val accountType get() = AccountType.values().firstOrNull { it.apiValue == typeOfAccount }

}
