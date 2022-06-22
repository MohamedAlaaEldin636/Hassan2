package com.maproductions.mohamedalaa.shared.presentation.myAccount

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.FileType

data class ItemFile(
    var id: Int?,
    @SerializedName("file") var fileUrl: String?,
    @SerializedName("file_type") var typeOfFile: String?,
) {
    val fileType get() = FileType.values().firstOrNull { it.apiValue == typeOfFile }
}

