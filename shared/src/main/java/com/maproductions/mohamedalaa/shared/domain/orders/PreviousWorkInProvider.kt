package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.FileType

/**
 * - file is either image or video
 */
data class PreviousWorkInProvider(
    var id: Int,
    @SerializedName("file") var fileUrl: String,
    @SerializedName("file_type") var typeOfFile: String,
) {
    val fileType get() = FileType.values().first { it.apiValue == typeOfFile }
}
