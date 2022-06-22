package com.maproductions.mohamedalaa.shared.core

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.maproductions.mohamedalaa.shared.core.di.module.OkHttpModule
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit

@GlideModule
class GlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(OkHttpModule.TIMEOUT_IN_SEC, TimeUnit.SECONDS)
        builder.writeTimeout(OkHttpModule.TIMEOUT_IN_SEC, TimeUnit.SECONDS)
        builder.connectTimeout(OkHttpModule.TIMEOUT_IN_SEC, TimeUnit.SECONDS)
        registry.append(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(builder.build())
        )
    }
}
