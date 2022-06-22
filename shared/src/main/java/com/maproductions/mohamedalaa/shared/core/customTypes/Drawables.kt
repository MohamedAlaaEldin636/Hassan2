package com.maproductions.mohamedalaa.shared.core.customTypes

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.databinding.BindingAdapter
import com.maproductions.mohamedalaa.shared.core.extensions.orZero

private fun getSolidRoundRect(@ColorInt solidColor: Int, radius: Float, strokeWidth: Float): Drawable {
    val outerRadii = floatArrayOf(
        radius,
        radius,
        radius,
        radius,
        radius,
        radius,
        radius,
        radius
    )

    val roundRectShape = RoundRectShape(outerRadii, null, null)
    val shapeDrawable = ShapeDrawable(roundRectShape)
    shapeDrawable.paint.color = solidColor
    shapeDrawable.paint.strokeWidth = strokeWidth

    return shapeDrawable
}

/*@BindingAdapter(
    value = [
        "view_setCustomSolidShapeBackground_colorString",
        "view_setCustomSolidShapeBackground_color",
        "view_setCustomSolidShapeBackground_radius",
        "view_setCustomSolidShapeBackground_stroke",
    ],
    requireAll = false
)
fun View.setCustomSolidShapeBackground(
    stringColor: String?,
    @ColorInt solidColor: Int?,
    radius: Float?,
    stroke: Float?,
) {
    val color = stringColor?.let {
        kotlin.runCatching { Color.parseColor(stringColor) }.getOrNull()
    } ?: solidColor ?: Color.WHITE

    background = getSolidRoundRect(
        color,
        radius.orZero(),
        stroke.orZero(),
    )
}*/

/*
internal fun getSolidRoundRect(@ColorInt solidColor: Int, @Px radiusInPx: Int): Drawable {
    val outerRadii = floatArrayOf(
        radius,
        radius,
        radius,
        radius,
        radius,
        radius,
        radius,
        radius
    )

    val roundRectShape = RoundRectShape(outerRadii, null, null)
    val shapeDrawable = ShapeDrawable(roundRectShape)
    shapeDrawable.paint.color = solidColor

    return shapeDrawable
}
 */
