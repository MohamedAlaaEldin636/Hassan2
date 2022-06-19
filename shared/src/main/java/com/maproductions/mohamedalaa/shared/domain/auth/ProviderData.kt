package com.maproductions.mohamedalaa.shared.domain.auth

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.AccountType
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeCategory

data class ProviderData(
    var id: Int = -1,
    @SerializedName("account_type") var typeOfAccount: String = AccountType.PROVIDER.apiValue,
    var verified: Int = 0,
    var approved: Int = 0,
    var premium: Int = 0,
    @SerializedName("orders_flag") var ordersFlag: Int = 0,

    var name: String? = null,
    var email: String? = null,

    var phone: String? = null,

    var latitude: String? = null,
    var longitude: String? = null,
    var address: String? = null,

    var wallet: Float = 0f,

    @SerializedName("birth_date") var birthDate: String = "2022-02-22",

    @SerializedName("relative_phone") var relativePhone: String = "",

    @SerializedName("token") var apiToken: String = "",

    @SerializedName("image") var imageUrl: String? = null,

    var services: List<ServiceInCategory>? = null,

    var category: String? = null
) {

    val stopReceivingOrders get() = ordersFlag == 1

    val isVerified get() = verified == 1

    val isApproved get() = approved == 1

    val isPremium get() = premium == 1

    val accountType get() = AccountType.values().first { it.apiValue == typeOfAccount }

}
