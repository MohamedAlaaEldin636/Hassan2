package com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("view_setVisibleOrInvisible")
fun View.setVisibleOrInvisible(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.INVISIBLE
}
