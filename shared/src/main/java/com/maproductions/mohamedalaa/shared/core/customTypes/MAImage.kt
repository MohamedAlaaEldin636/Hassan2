package com.maproductions.mohamedalaa.shared.core.customTypes

import android.net.Uri

sealed class MAImage {

    abstract fun getId(): String

    data class IUri(var uri: Uri) : MAImage() {
        override fun getId(): String = uri.toString()
    }
    data class ILink(var url: String) : MAImage() {
        override fun getId(): String = url
    }
}
