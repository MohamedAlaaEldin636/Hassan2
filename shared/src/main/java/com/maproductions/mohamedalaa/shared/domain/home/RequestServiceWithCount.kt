package com.maproductions.mohamedalaa.shared.domain.home

import androidx.annotation.IntRange
import com.google.gson.annotations.SerializedName

data class RequestServiceWithCount(
    @SerializedName("service_id") var serviceId: Int,
    var price: Float,
    @IntRange(from = 1) var count: Int,
)
