package com.maproductions.mohamedalaa.shared.domain.settings

import com.google.gson.annotations.SerializedName

data class ResponsePoints(
    @SerializedName("app_points") var appPoints: String,
    @SerializedName("my_points") var myPoints: Int,
    var link: String,
)
/*
"app_points": "10",
        "my_points": 0,
        "link": "#6280b11da45b6"
 */