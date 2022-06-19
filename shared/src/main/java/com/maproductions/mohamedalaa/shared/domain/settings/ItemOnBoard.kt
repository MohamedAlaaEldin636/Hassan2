package com.maproductions.mohamedalaa.shared.domain.settings

import com.google.gson.annotations.SerializedName

data class ItemOnBoard(
    @SerializedName("image") var imageUrl: String,
    @SerializedName("text") var textAsHtml: String,
)
