package com.maproductions.mohamedalaa.shared.domain.home

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.AccountType
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.customTypes.SliderLinkType

data class SliderHomeUser(
    var id: Int,
    var type: String,
    @SerializedName("link_type") var typeOfLink: String,
    @SerializedName("link_id") var linkId: Int,
    @SerializedName("image") var imageUrl: String,
    var category: SliderHomeCategory?,
) {
    val linkType get() = SliderLinkType.values().first { it.apiValue == typeOfLink }

    val accountType get() = AccountType.values().first { it.apiValue == type }
}
