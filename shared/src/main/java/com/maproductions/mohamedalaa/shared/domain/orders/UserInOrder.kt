package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName

data class UserInOrder(
    var id: Int,
    var name: String,
    var phone: String,
    @SerializedName("image") var imageUrl: String?,
)
