package com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("editText_setOnEditorActionListenerBA")
fun EditText.setOnEditorActionListenerBA(listener: TextView.OnEditorActionListener) {
	setOnEditorActionListener(listener)
}
