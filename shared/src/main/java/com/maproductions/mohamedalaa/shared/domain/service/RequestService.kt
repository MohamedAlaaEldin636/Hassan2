package com.maproductions.mohamedalaa.shared.domain.service

import com.google.gson.annotations.SerializedName

data class RequestService(
    @SerializedName("service_id") var service_id: Int,
    @SerializedName("price") var price: Float,
    @SerializedName("count") var count: Int,
)

data class MM(
    var services: List<RequestService>
)
