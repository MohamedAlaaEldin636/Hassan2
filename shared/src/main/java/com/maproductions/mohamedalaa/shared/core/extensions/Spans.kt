package com.maproductions.mohamedalaa.shared.core.extensions

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

fun createClickableSpan(onClickAction: (View) -> Unit): ClickableSpan = object : ClickableSpan() {
    override fun onClick(widget: View) {
        onClickAction(widget)
    }

    override fun updateDrawState(ds: TextPaint) {}
}
