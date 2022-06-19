package com.maproductions.mohamedalaa.shared.core.customTypes

data class MABaseResponse<T>(
    val data: T?,
    val message: String,
    val code: Int,
)
