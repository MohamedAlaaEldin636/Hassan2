package com.maproductions.mohamedalaa.shared.core.customTypes

import androidx.annotation.StringRes
import com.maproductions.mohamedalaa.shared.R

class ErrorMsgNoneSingleAll(@StringRes private val allStringRes: Int = R.string.all_fields_required) {

    @get:StringRes
    @setparam:StringRes
    var value: Int? = null
        set(value) {
            field = if (field == null) value else allStringRes
        }

}
