package com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import com.maproductions.mohamedalaa.shared.core.extensions.dpToPx
import kotlin.math.roundToInt

@BindingAdapter("materialCardView_setStrokeWidthIntInDpBA")
fun MaterialCardView.setStrokeWidthIntInDpBA(widthInDp: Int?) {
	strokeWidth = context?.dpToPx(widthInDp?.toFloat() ?: return)?.roundToInt() ?: return
}

@BindingAdapter("materialCardView_setStrokeWidthBA")
fun MaterialCardView.setStrokeWidthBA(@Px widthInPixels: Int?) {
	strokeWidth = widthInPixels ?: return
}

@BindingAdapter("materialCardView_setStrokeColorResBA")
fun MaterialCardView.setStrokeColorResBA(@ColorRes colorRes: Int?) {
	val color = kotlin.runCatching {
		ContextCompat.getColor(context, colorRes ?: return)
	}.getOrNull()

	strokeColor = color ?: return
}

@BindingAdapter("materialCardView_setCardBackgroundColorBA")
fun MaterialCardView.setCardBackgroundColorBA(@ColorInt color: Int?) {
	setCardBackgroundColor(color ?: return)
}

@BindingAdapter("materialCardView_setCardBackgroundColorResBA")
fun MaterialCardView.setCardBackgroundColorResBA(@ColorRes colorRes: Int?) {
	val color = kotlin.runCatching {
		ContextCompat.getColor(context, colorRes ?: return)
	}.getOrNull()

	setCardBackgroundColor(color ?: return)
}

@BindingAdapter("materialCardView_setCardElevationBA")
fun MaterialCardView.setCardElevationBA(@Px value: Float?) {
	cardElevation = value ?: return
}
