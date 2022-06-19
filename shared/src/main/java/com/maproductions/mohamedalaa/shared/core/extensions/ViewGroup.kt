package com.maproductions.mohamedalaa.shared.core.extensions

import android.view.View
import android.view.ViewGroup

inline fun <reified V : View> ViewGroup.removeAllViewsInstanceOf() {
    val toBeRemovedIndices = mutableListOf<Int>()
    for (index in 0 until childCount) {
        if (getChildAt(index) is V) {
            toBeRemovedIndices += index
        }
    }

    toBeRemovedIndices.sortDescending()

    for (index in toBeRemovedIndices) {
        removeViewAt(index)
    }
}
