package com.maproductions.mohamedalaa.shared.domain.auth

import com.google.gson.annotations.SerializedName

data class ProviderFile(
    var id: Int?,
    @SerializedName("file") var fileUrl: String?,
    /** image_front_id */
    var type: String?,
    var status: String?,
) {

    val isStatusApproved get() = status == "approved"

}
/*
{
                "id": 252,
                "file": "https://hassan.my-staff.net/storage//tmp/phpiypnxy",
                "file_type": "image",
                "mediable_type": "App\\Models\\User",
                "status": "approved",
                "type": "image_front_id"
            }
 */
