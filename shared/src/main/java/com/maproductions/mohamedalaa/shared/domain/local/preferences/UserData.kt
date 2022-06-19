package com.maproductions.mohamedalaa.shared.domain.local.preferences

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationData

/**
 * @param email optional
 */
data class UserData(
    var id: Int = -1,

    var name: String = "",
    var email: String = "",

    var latitude: String = "",
    var longitude: String = "",
    var address: String = "",

    var phone: String = "",
    var imageUrl: String = "",
) {

    companion object {
        fun fromLocationDataOnly(locationData: LocationData): UserData {
            return UserData(
                latitude = locationData.latitude,
                longitude = locationData.longitude,
                address = locationData.address,
            )
        }
    }

}
