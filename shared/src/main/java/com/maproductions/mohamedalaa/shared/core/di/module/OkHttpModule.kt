package com.maproductions.mohamedalaa.shared.core.di.module

import android.content.Context
import android.os.Build
import com.maproductions.mohamedalaa.shared.core.di.module.qualifiers.HeadersInterceptor
import com.maproductions.mohamedalaa.shared.core.extensions.getProjectCurrentLocale
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {

    const val TIMEOUT_IN_SEC = 30L

    private const val HEADER_KEY_LANGUAGE = "language"
    const val HEADER_KEY_AUTHORIZATION = "Authorization"

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @HeadersInterceptor headersInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            readTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)

            addInterceptor(headersInterceptor)

            addNetworkInterceptor(httpLoggingInterceptor)
        }.build()
    }

    @HeadersInterceptor
    @Provides
    fun provideHeadersInterceptor(
        @ApplicationContext context: Context
    ): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()

            val url = request.url.toString()

            val builder = request.newBuilder()

            if (url.startsWith(RetrofitModule.BASE_URL)) {
                builder.addHeader(HEADER_KEY_LANGUAGE, /*"en"*/context.getProjectCurrentLocale())

                builder.addHeader("Accept", "application/json")
                //builder.addHeader("Content-Type", "application/json")

                request.header(HEADER_KEY_AUTHORIZATION)?.also {
                    builder.removeHeader(HEADER_KEY_AUTHORIZATION)
                    if (it.isNotEmpty()) {
                        builder.addHeader(HEADER_KEY_AUTHORIZATION, "Bearer $it")
                    }
                }
            }

            chain.proceed(builder.build())
        }
    }

    /**
     * - Used for debugging.
     */
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Timber.d(message)
        }.also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    }

}
