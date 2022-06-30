package com.maproductions.mohamedalaa.shared.core.extensions

fun Int.minLengthOrPrefixZeros(length: Int): String {
    val num = toString()

    return if (num.length >= length) num else "${"0".repeat(length - num.length)}$num"
}

fun String?.isValidIraqPhoneWithoutPrefix964(): Boolean {
    // 10 max length & 8 is min
    return this?.let { length in 8..10 } ?: false
}
