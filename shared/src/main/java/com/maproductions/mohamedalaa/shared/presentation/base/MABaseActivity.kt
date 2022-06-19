package com.maproductions.mohamedalaa.shared.presentation.base

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.maproductions.mohamedalaa.shared.core.customTypes.LanguagesHelper
import com.maproductions.mohamedalaa.shared.core.customTypes.MyContextWrapper
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl
import java.util.*

abstract class MABaseActivity<VDB : ViewDataBinding> : AppCompatActivity() {

    companion object {
        const val APPLICATION_DEFAULT_LANGUAGE = "ar"
    }

    var binding: VDB? = null

    override fun attachBaseContext(newBase: Context?) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 && newBase != null) {
            super.attachBaseContext(MyContextWrapper.wrap(newBase, LanguagesHelper.getCurrentLanguage()))
        }else {
            super.attachBaseContext(newBase)
        }
    }

    @CallSuper
    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LanguagesHelper.changeLanguage(this, LanguagesHelper.getCurrentLanguage())
        LanguagesHelper.changeLanguage(applicationContext, LanguagesHelper.getCurrentLanguage())

        binding = DataBindingUtil.setContentView(this, getLayoutId())
        initializeBindingVariables()
        binding?.lifecycleOwner = this

        setupsInOnCreate()
    }

    protected open fun initializeBindingVariables() {}

    /** Called inside [onCreate] after initializing [binding] isa. */
    protected open fun setupsInOnCreate() {}

    @LayoutRes
    abstract fun getLayoutId(): Int

}
