package com.maproductions.mohamedalaa.shared.core.extensions

fun String?.orIfNullOrEmpty(fallback: String): String {
    return if (isNullOrEmpty()) fallback else this
}
