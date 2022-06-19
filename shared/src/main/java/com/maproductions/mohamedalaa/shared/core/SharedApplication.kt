package com.maproductions.mohamedalaa.shared.core

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.multidex.MultiDex
import com.maproductions.mohamedalaa.shared.core.customTypes.StaticValues
import es.dmoral.toasty.Toasty
import timber.log.Timber

abstract class SharedApplication : Application() {

    abstract val isUserNotProvider: Boolean

    abstract val mainActivityClazz: Class<*>

    private var toast: Toast? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    @CallSuper
    override fun onCreate() {
        StaticValues.isUserNotProvider = isUserNotProvider

        StaticValues.mainActivityClazz = mainActivityClazz

        super.onCreate()

        Timber.plant(LineNumberDebugTree())

        setupToasting()
    }

    private fun setupToasting() {
        Toasty.Config.getInstance().allowQueue(false).apply()
    }

    fun showSuccessToast(msg: String) = showToast(Toasty.success(this, msg, Toast.LENGTH_SHORT, true))

    fun showErrorToast(msg: String) = showToast(Toasty.error(this, msg, Toast.LENGTH_SHORT, true))

    fun showNormalToast(msg: String) = showToast(Toasty.normal(this, msg, Toast.LENGTH_SHORT, null, false))

    private fun showToast(toast: Toast) {
        this.toast?.cancel()
        this.toast = toast
        this.toast?.show()
    }

    class LineNumberDebugTree : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String {
            return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
        }
    }

}