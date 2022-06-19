package com.maproductions.mohamedalaa.shared.domain.home

import com.google.gson.annotations.SerializedName

data class SliderHomeCategory(
    var id: Int,
    @SerializedName("image") var imageUrl: String,
    var name: String,
    @SerializedName("delivery_fees") var deliveryFees: Float,
    @SerializedName("order_min_price") var orderMinPrice: Float,
    @SerializedName("order_min_price_for_extra") var orderMinPriceForExtra: Float,
)
