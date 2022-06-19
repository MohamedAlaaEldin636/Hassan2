package com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter

import android.content.res.ColorStateList
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton

@BindingAdapter("materialButton_backgroundTintBA")
fun MaterialButton.setBackgroundTintBA(@ColorInt color: Int?) {
    backgroundTintList = ColorStateList.valueOf(color ?: return)
}
