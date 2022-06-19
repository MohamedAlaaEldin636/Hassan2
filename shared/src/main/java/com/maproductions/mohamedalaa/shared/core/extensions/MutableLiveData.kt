package com.maproductions.mohamedalaa.shared.core.extensions

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Boolean>.toggleValueIfNotNull() {
    value = !(value ?: return)
}
