package com.maproductions.mohamedalaa.shared.domain.location

import com.google.gson.annotations.SerializedName

data class ResponsePusherLocation(
    @SerializedName("lat") var latitude: String,
    @SerializedName("lng") var longitude: String,
)
