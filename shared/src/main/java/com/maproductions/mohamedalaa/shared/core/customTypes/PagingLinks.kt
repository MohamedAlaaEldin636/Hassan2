package com.maproductions.mohamedalaa.shared.core.customTypes

import com.google.gson.annotations.SerializedName

data class PagingLinks(
    @SerializedName("first") var firstPageUrl: String,
    @SerializedName("last") var lastPageUrl: String,
    @SerializedName("next") var nextPageUrl: String,
    @SerializedName("prev") var prevPageUrl: String,
)
/*
"links": {
        "first": "https://hassan.my-staff.net/api/v1/search-text-result?page=1",
        "last": "https://hassan.my-staff.net/api/v1/search-text-result?page=1",
        "prev": null,
        "next": null
    }
*/
