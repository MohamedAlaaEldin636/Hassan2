package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName

data class ItemCancellationReason(
    var id: Int?,
    @SerializedName("image") var imageUrl: String?,
    var text: String?,
)
/*
{
            "id": 12,
            "image": "https://hassan.my-staff.net/uploads/logo.png",
            "text": "سعر خاطئ"
        }
 */
