package com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("textView_setDrawableEndCompatBA")
fun TextView.setDrawableEndCompatBA(drawable: Drawable?) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
}

@BindingAdapter("textView_setDrawableResEndCompatBA")
fun TextView.setDrawableResEndCompatBA(@DrawableRes drawableRes: Int?) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, drawableRes ?: return, 0)
}

@BindingAdapter("textView_setDrawableResStartCompatBA")
fun TextView.setDrawableResStartCompatBA(@DrawableRes drawableRes: Int?) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(drawableRes ?: return, 0, 0, 0)
}

@BindingAdapter("textView_setTextHtml")
fun TextView.setTextHtml(textAsHtml: String?) {
    text = HtmlCompat.fromHtml(
        textAsHtml.orEmpty(),
        HtmlCompat.FROM_HTML_MODE_COMPACT
    )
}
