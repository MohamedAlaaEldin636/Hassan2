package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun RequestBuilder<Bitmap>.intoBitmap(): Bitmap? {
	return suspendCoroutine { continuation ->
		into(object : CustomTarget<Bitmap>() {
			override fun onResourceReady(
				resource: Bitmap,
				transition: Transition<in Bitmap>?
			) {
				kotlin.runCatching {
					continuation.resume(resource)
				}.getOrElse {
					kotlin.runCatching {
						continuation.resume(null)
					}
				}
			}

			override fun onLoadFailed(errorDrawable: Drawable?) {
				kotlin.runCatching {
					continuation.resume(null)
				}
			}

			override fun onLoadCleared(placeholder: Drawable?) {
				kotlin.runCatching {
					continuation.resume(placeholder?.toBitmap())
				}
			}
		})
	}
}

fun <T> RequestBuilder<T>.listenerOnResourceReady(onResourceReady: (T?) -> Unit): RequestBuilder<T> = listener(object :RequestListener<T?> {
	override fun onLoadFailed(
		e: GlideException?,
		model: Any?,
		target: Target<T?>?,
		isFirstResource: Boolean
	): Boolean {
		return false
	}

	override fun onResourceReady(
		resource: T?,
		model: Any?,
		target: Target<T?>?,
		dataSource: DataSource?,
		isFirstResource: Boolean
	): Boolean {
		onResourceReady(resource)

		return false
	}
})

fun <T> RequestBuilder<T>.listenerOnResourceReadyFitXY(imageView: ImageView?): RequestBuilder<T> = listenerOnResourceReady {
	if (it != null) {
		imageView?.scaleType = ImageView.ScaleType.FIT_XY
	}
}
