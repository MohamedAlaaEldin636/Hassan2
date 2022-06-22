package com.maproductions.mohamedalaa.shared.domain.home

import com.google.gson.annotations.SerializedName

data class ResponseCheckPromoCode(
    var id: Int,
    var code: String,
    @SerializedName("discount_type") var discountType: String,
    var value: Float,
) {

    val isPercent get() = discountType == "percent"

}


//"promo":{"id":30,"code":"MHJFV","discount_type":"percent","value":50}