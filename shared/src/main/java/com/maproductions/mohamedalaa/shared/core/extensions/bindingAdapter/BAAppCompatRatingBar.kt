package com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter

import androidx.appcompat.widget.AppCompatRatingBar
import androidx.databinding.BindingAdapter
import timber.log.Timber

@BindingAdapter("appCompatRatingBar_setProgress")
fun AppCompatRatingBar.setProgressBA(progress: Int?) {
    val actualProgress = (progress ?: 0).coerceAtLeast(0)
        .coerceAtMost(100)

    max = 100
    this.progress = actualProgress
}
