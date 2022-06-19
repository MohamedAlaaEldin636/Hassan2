package com.maproductions.mohamedalaa.hassanp.core

import com.maproductions.mohamedalaa.hassanp.presentation.main.MainActivity
import com.maproductions.mohamedalaa.shared.core.SharedApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : SharedApplication() {
    override val isUserNotProvider: Boolean = false

    override val mainActivityClazz: Class<*> = MainActivity::class.java
}
