package com.maproductions.mohamedalaa.shared.core.customTypes

data class LocationData(
    val latitude: String,
    val longitude: String,
    val address: String,
)

fun LocationData?.orEmpty() = this?.copy() ?: LocationData("", "", "")
