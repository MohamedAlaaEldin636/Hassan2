package com.maproductions.mohamedalaa.shared.domain.home

import com.google.gson.annotations.SerializedName

data class DeliveryData(
    var deliveryFees: Float,
    var orderMinPrice: Float,
    var orderMinPriceForExtra: Float,
)
