package com.maproductions.mohamedalaa.shared.domain.settings

import com.google.gson.annotations.SerializedName

data class ResponseAddress(
    var id: Int,

    var title: String,
    @SerializedName("street_name") var streetName: String,
    @SerializedName("extra_description") var extraDescription: String,

    var latitude: Double,
    var longitude: Double,
    var address: String,

    var city: String,
    var area: String,
)
