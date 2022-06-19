package com.maproductions.mohamedalaa.shared.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsSplash
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.presentation.main.SharedMainActivity
import com.maproductions.mohamedalaa.shared.presentation.main.viewModels.MainViewModel
import javax.inject.Inject

abstract class MABaseFragment<VDB : ViewDataBinding> : Fragment() {

    var binding: VDB? = null

    @Inject
    lateinit var prefsSplash: PrefsSplash

    @Inject
    lateinit var prefsAccount: PrefsAccount

    private var _activityViewModel: MainViewModel? = null

    val sharedMainActivity: SharedMainActivity?
        get() = activity as? SharedMainActivity

    val activityViewModel: MainViewModel?
        get() {
            return try {
                _activityViewModel ?: synchronized(this) {
                    _activityViewModel ?: safeActivityViewModels<MainViewModel>().value.also { _activityViewModel = it }
                }
            }catch (throwable: Throwable) {
                safeActivityViewModels<MainViewModel>().value.also { _activityViewModel = it }
            }
        }

    private inline fun <reified VM : ViewModel> Fragment.safeActivityViewModels(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM?> = if (isAdded) createViewModelLazy(
        VM::class, { requireActivity().viewModelStore },
        factoryProducer ?: { requireActivity().defaultViewModelProviderFactory }
    ) else lazy<VM?> { null }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        initializeBindingVariables()
        binding?.lifecycleOwner = viewLifecycleOwner

        return binding?.root
    }

    protected open fun initializeBindingVariables() {}

    @CallSuper
    override fun onDestroyView() {
        binding = null

        super.onDestroyView()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

}
