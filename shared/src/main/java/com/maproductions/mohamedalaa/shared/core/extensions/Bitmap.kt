package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.getSystemService
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import timber.log.Timber
import kotlin.math.absoluteValue

fun View.toBitmap(): Bitmap? {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

    val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)

    layout(0, 0, measuredWidth, measuredHeight)

    draw(canvas)
    /*
    view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
    Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
            Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    view.draw(canvas);
    return bitmap;
     */

    return bitmap
}

/*
fun View.toBitmap(context: Context = this.context): Bitmap? {
    val windowManager = context.getSystemService<WindowManager>() ?: return null
    val (width, height) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val metrics = windowManager.currentWindowMetrics
        val insets = metrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        Pair(
            metrics.bounds.width().absoluteValue - insets.left - insets.right,
            metrics.bounds.height().absoluteValue - insets.bottom - insets.top
        )
    }else {
        val displayMetrics = DisplayMetrics()

        windowManager.defaultDisplay?.getMetrics(displayMetrics) ?: return null

        displayMetrics.widthPixels to displayMetrics.heightPixels
    }

    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    measure(
        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.UNSPECIFIED)
    )
    layout(0, 0, measuredWidth, measuredHeight)

    val bitmap = createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888).applyCanvas {
        layout(0, 0, measuredWidth, measuredHeight)
        this@toBitmap.draw(this)
    }

    Timber.e("width $measuredWidth, height $measuredHeight")

    layout(0, 0, measuredWidth, measuredHeight)
    val c = Canvas(bitmap)
    layout(0, 0, measuredWidth, measuredHeight)
    draw(c)
    layout(0, 0, measuredWidth, measuredHeight)

    return bitmap
}
 */
