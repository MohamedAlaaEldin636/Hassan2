package com.maproductions.mohamedalaa.hassanu.core

import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.maproductions.mohamedalaa.hassanu.presentation.main.MainActivity
import com.maproductions.mohamedalaa.shared.core.SharedApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : SharedApplication() {
    override val isUserNotProvider: Boolean = true

    override val mainActivityClazz: Class<*> = MainActivity::class.java

    override fun onCreate() {
        super.onCreate()

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }

}
