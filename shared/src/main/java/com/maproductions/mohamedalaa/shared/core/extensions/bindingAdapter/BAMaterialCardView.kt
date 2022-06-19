package com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView

@BindingAdapter("materialCardView_setStrokeWidthBA")
fun MaterialCardView.setStrokeWidthBA(@Px widthInPixels: Int?) {
	strokeWidth = widthInPixels ?: return
}

@BindingAdapter("materialCardView_setCardElevationBA")
fun MaterialCardView.setCardElevationBA(@Px value: Float?) {
	cardElevation = value ?: return
}
