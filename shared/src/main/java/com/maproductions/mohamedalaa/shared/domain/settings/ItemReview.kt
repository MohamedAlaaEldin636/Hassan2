package com.maproductions.mohamedalaa.shared.domain.settings

import com.google.gson.annotations.SerializedName

data class ItemReview(
    var rate: Float?,
    var review: String?,
    @SerializedName("user_name") var userName: String?,
    @SerializedName("created_at") var createdAt: String?,
)
