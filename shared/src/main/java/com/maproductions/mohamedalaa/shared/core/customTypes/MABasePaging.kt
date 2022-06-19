package com.maproductions.mohamedalaa.shared.core.customTypes

import com.google.gson.annotations.SerializedName

data class MABasePaging<T>(
    val data: List<T>?,

    val links: PagingLinks? = null,
)
