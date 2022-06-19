package com.maproductions.mohamedalaa.shared.core.customTypes

data class ImageOrVideo(
    var fileUrl: String,
    var typeOfFile: String,
) {
    val fileType get() = FileType.values().first { it.apiValue == typeOfFile }
}
