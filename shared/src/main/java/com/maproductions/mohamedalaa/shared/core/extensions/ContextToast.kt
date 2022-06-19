package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.Context
import android.widget.Toast
import com.maproductions.mohamedalaa.shared.core.SharedApplication

fun Context.showSuccessToast(msg: String) {
    (applicationContext as? SharedApplication)?.showSuccessToast(msg) ?: simpleToast(msg)
}

fun Context.showErrorToast(msg: String) {
    (applicationContext as? SharedApplication)?.showErrorToast(msg) ?: simpleToast(msg)
}

fun Context.showNormalToast(msg: String) {
    (applicationContext as? SharedApplication)?.showNormalToast(msg) ?: simpleToast(msg)
}

private fun Context.simpleToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
