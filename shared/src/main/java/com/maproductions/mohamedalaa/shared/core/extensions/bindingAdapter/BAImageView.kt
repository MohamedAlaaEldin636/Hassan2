package com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter

import android.net.Uri
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage

@BindingAdapter("imageView_setSRCDrawableResBA")
fun ImageView.setSRCDrawableResBA(@DrawableRes drawableRes: Int?) {
    setImageResource(drawableRes ?: 0)
}

@BindingAdapter("imageView_setUrlViaGlideOrIgnore")
fun ImageView.setUrlViaGlideOrIgnore(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
    }
}

@BindingAdapter("imageView_setUrlViaGlideOrIgnore")
fun ImageView.setUriViaGlideOrIgnore(uri: Uri?) {
    if (uri != null) {
        Glide.with(context)
            .load(uri)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
    }
}

@BindingAdapter("imageView_setUrlViaGlideOrPlaceholder")
fun ImageView.setUrlViaGlideOrPlaceholder(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
    }else {
        setImageResource(R.drawable.ic_logo_app_placeholder)
    }
}

@BindingAdapter("imageView_setUriViaGlideOrPlaceholder")
fun ImageView.setUriViaGlideOrPlaceholder(uri: Uri?) {
    if (uri != null) {
        Glide.with(context)
            .load(uri)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
    }else {
        setImageResource(R.drawable.ic_logo_app_placeholder)
    }
}

@BindingAdapter("imageView_setVideoUriViaGlideOrClear")
fun ImageView.setVideoUriViaGlideOrClear(uri: Uri?) {
    if (uri != null) {
        Glide.with(context)
            .load(uri)
            .apply(RequestOptions().frame(1).centerCrop())
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
    }else {
        setImageDrawable(null)
    }
}

@BindingAdapter("imageView_setVideoUriViaGlideOrPlaceholderMAImage")
fun ImageView.setVideoUriViaGlideOrPlaceholderMAImage(maImage: MAImage?) {
    when (maImage) {
        is MAImage.ILink -> Glide.with(context)
            .load(maImage.url)
            .apply(RequestOptions().frame(1).centerCrop())
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
        is MAImage.IUri -> Glide.with(context)
            .load(maImage.uri)
            .apply(RequestOptions().frame(1).centerCrop())
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
        null -> setImageResource(R.drawable.ic_logo_app_placeholder)
    }
}

@BindingAdapter("imageView_setImageUriViaGlideOrPlaceholderMAImage")
fun ImageView.setImageUriViaGlideOrPlaceholderMAImage(maImage: MAImage?) {
    when (maImage) {
        is MAImage.ILink -> Glide.with(context)
            .load(maImage.url)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
        is MAImage.IUri -> Glide.with(context)
            .load(maImage.uri)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
        null -> setImageResource(R.drawable.ic_logo_app_placeholder)
    }
}

fun ImageView.setImageUriViaGlideOrPlaceholderMAImage3(maImage: MAImage?) {
    when (maImage) {
        is MAImage.ILink -> Glide.with(context)
            .load(maImage.url)
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
        is MAImage.IUri -> Glide.with(context)
            .load(maImage.uri)
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(this)
        null -> setImageResource(R.drawable.ic_logo_app_placeholder)
    }
}
