package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName

data class ReviewInProvider(
    var rate: Float?,
    var review: String?,
    @SerializedName("user_name") var userName: String?,
    @SerializedName("image") var userImageUrl: String?,
    @SerializedName("created_at") var createdAt: String?,
)
