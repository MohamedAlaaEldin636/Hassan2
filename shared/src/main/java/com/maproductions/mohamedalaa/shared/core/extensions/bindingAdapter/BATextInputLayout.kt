package com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.maproductions.mohamedalaa.shared.R

@BindingAdapter("textInputLayout_setEndIconAsCloseOrEmptyBA", "textInputLayout_onEndIconClicksBA")
fun TextInputLayout.setEndIconAsCloseOrEmptyBA(performSet: Boolean?, listener: View.OnClickListener?) {
    endIconMode = if (performSet == true) TextInputLayout.END_ICON_CUSTOM else TextInputLayout.END_ICON_NONE
    if (performSet == true) {
        endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_round_close_24)

        setEndIconOnClickListener(listener)
    }
}

@BindingAdapter("textInputLayout_onEndIconClicksBA2")
fun TextInputLayout.setEndIconAsCloseOrEmptyBA2(listener: View.OnClickListener?) {
    setEndIconOnClickListener(listener)
}
