package com.maproductions.mohamedalaa.shared.presentation.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.presentation.main.SharedMainActivity
import com.maproductions.mohamedalaa.shared.presentation.main.viewModels.MainViewModel
import javax.inject.Inject

abstract class MADialogFragment<VDB : ViewDataBinding> : DialogFragment() {

    open val widthIsMatchParent: Boolean = true
    open val heightIsMatchParent: Boolean = false
    open val windowGravity: Int = Gravity.CENTER
    open val canceledOnTouchOutside: Boolean = true

    var binding: VDB? = null

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

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        initializeBindingVariables()
        binding?.lifecycleOwner = viewLifecycleOwner

        return binding?.root
    }

    protected open fun initializeBindingVariables() {}

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return customOnCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.apply {
                setLayout(
                    if (widthIsMatchParent) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT,
                    if (heightIsMatchParent) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT,
                )

                setGravity(windowGravity)

                onCreateDialogWindowChanges(this)
            }

            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)

            dialog.setOnCancelListener { onCancelListener() }

            dialog.setOnDismissListener { onDismissListener() }

            dialog.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed()

                    true
                }else {
                    false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.apply {
            setLayout(
                if (widthIsMatchParent) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT,
                if (heightIsMatchParent) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT,
            )

            setGravity(windowGravity)
        }
    }

    protected open fun customOnCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    final override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    /** calls [dismiss] if [canceledOnTouchOutside] is `true` otherwise does nothing isa. */
    open fun onBackPressed() {
        if (canceledOnTouchOutside) {
            dismiss()
        }
    }

    open fun onCreateDialogWindowChanges(window: Window) {}

    open fun onCancelListener() {}

    /**
     * - Covers all cases, such as [onCancelListener] & [onBackPressed]
     */
    open fun onDismissListener() {}

}
