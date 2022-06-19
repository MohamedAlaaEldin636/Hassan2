package com.maproductions.mohamedalaa.shared.domain.auth

import com.google.gson.annotations.SerializedName

data class ResponseUpdateProfile(
    var id: Int,
    @SerializedName("account_type") var typeOfAccount: String,
    var verified: Int,

    var name: String?,
    var email: String?,

    var phone: String?,

    var latitude: String?,
    var longitude: String?,
    var address: String?,

    @SerializedName("image") var imageUrl: String?,
)
