package com.maproductions.mohamedalaa.hassanp.core

import androidx.fragment.app.Fragment
import com.maproductions.mohamedalaa.hassanp.presentation.main.MainActivity
import com.maproductions.mohamedalaa.shared.core.SharedApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : SharedApplication() {
    override val isUserNotProvider: Boolean = false

    override val mainActivityClazz: Class<*> = MainActivity::class.java

    private var sendLocationToApiOnOpenApp = true

    fun mustSendLocationToApiOnOpenAppAndIfSoNotAgainWhileAppIsOpened(): Boolean {
        val value = sendLocationToApiOnOpenApp

        if (value) {
            sendLocationToApiOnOpenApp = false
        }

        return value
    }
}

fun Fragment.shouldUpdateProfileLocationToApi(): Boolean {
    return (context?.applicationContext as? MyApplication)
        ?.mustSendLocationToApiOnOpenAppAndIfSoNotAgainWhileAppIsOpened() ?: false
}
