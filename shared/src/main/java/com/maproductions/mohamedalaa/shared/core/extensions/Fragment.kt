package com.maproductions.mohamedalaa.shared.core.extensions

import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.GlobalError
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.customTypes.MAResult
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import com.maproductions.mohamedalaa.shared.presentation.main.SharedMainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

fun Fragment.getMyDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(requireContext(), id)!!

fun <VDB : ViewDataBinding, F : MABaseFragment<VDB>, T> F.handleRetryAbleFlowWithMustHaveResultWithNullability(
    retryAbleFlow: RetryAbleFlow<T>,
    onSuccess: (MABaseResponse<T>) -> Unit,
) {
    // Used to not keep repeating even after collection is done isa.
    var job: Job? = null
    job = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            retryAbleFlow.value.collectLatest {
                when (it) {
                    is MAResult.Loading -> {
                        activityViewModel?.globalLoading?.value = true
                    }
                    is MAResult.Success -> {
                        activityViewModel?.globalLoading?.value = false

                        onSuccess(it.value)
                    }
                    is MAResult.Failure -> {
                        Timber.e("failure is $it")

                        activityViewModel?.globalLoading?.value = false

                        if (it.failureStatus == MAResult.Failure.Status.TOKEN_EXPIRED) {
                            prefsAccount.logOut()

                            val navController = findNavControllerOfProject()

                            navController.popAllBackStacks()

                            navController.navigateDeepLinkWithoutOptions(
                                "fragment-dest",
                                "com.grand.hassan.shared.log.in",
                            )

                            return@collectLatest
                        }
                        /* else if -> MAResult.Failure.Status.ACTIVATION_NOT_VERIFIED ->
                            not occurred in this app as it's a must to do it verify code each time or skip it if provider login isa */
                        /* else -> show retry able dialog as done below */

                        activityViewModel?.globalError?.value = GlobalError.Show(it.message)

                        activityViewModel?.globalError?.observe(viewLifecycleOwner, object :
                            Observer<GlobalError> {
                            override fun onChanged(globalError: GlobalError?) {
                                if (globalError is GlobalError.Retry) {
                                    activityViewModel?.globalError?.removeObserver(this)

                                    activityViewModel?.globalError?.value = GlobalError.Cancel

                                    retryAbleFlow.retry()

                                    Handler(Looper.getMainLooper()).post {
                                        handleRetryAbleFlowWithMustHaveResultWithNullability(retryAbleFlow, onSuccess)
                                    }
                                }
                            }
                        })
                    }
                }
            }
            job?.cancel()
        }
    }
}

fun <VDB : ViewDataBinding, F : MADialogFragment<VDB>, T> F.handleRetryAbleFlowWithMustHaveResultWithNullability(
    retryAbleFlow: RetryAbleFlow<T>,
    onSuccess: (MABaseResponse<T>) -> Unit,
) {
    // Used to not keep repeating even after collection is done isa.
    var job: Job? = null
    job = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            retryAbleFlow.value.collectLatest {
                when (it) {
                    is MAResult.Loading -> {
                        activityViewModel?.globalLoading?.value = true
                    }
                    is MAResult.Success -> {
                        activityViewModel?.globalLoading?.value = false

                        onSuccess(it.value)
                    }
                    is MAResult.Failure -> {
                        Timber.e("failure is $it")

                        activityViewModel?.globalLoading?.value = false

                        if (it.failureStatus == MAResult.Failure.Status.TOKEN_EXPIRED) {
                            prefsAccount.logOut()

                            val navController = findNavControllerOfProject()

                            navController.popAllBackStacks()

                            navController.navigateDeepLinkWithoutOptions(
                                "fragment-dest",
                                "com.grand.hassan.shared.log.in",
                            )

                            return@collectLatest
                        }
                        /* else if -> MAResult.Failure.Status.ACTIVATION_NOT_VERIFIED ->
                            not occurred in this app as it's a must to do it verify code each time or skip it if provider login isa */
                        /* else -> show retry able dialog as done below */

                        activityViewModel?.globalError?.value = GlobalError.Show(it.message)

                        activityViewModel?.globalError?.observe(viewLifecycleOwner, object :
                            Observer<GlobalError> {
                            override fun onChanged(globalError: GlobalError?) {
                                if (globalError is GlobalError.Retry) {
                                    activityViewModel?.globalError?.removeObserver(this)

                                    activityViewModel?.globalError?.value = GlobalError.Cancel

                                    retryAbleFlow.retry()

                                    Handler(Looper.getMainLooper()).post {
                                        handleRetryAbleFlowWithMustHaveResultWithNullability(retryAbleFlow, onSuccess)
                                    }
                                }
                            }
                        })
                    }
                }
            }
            job?.cancel()
        }
    }
}

fun <T> MABaseFragment<*>.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
    afterShowingLoading: suspend () -> MAResult.Immediate<MABaseResponse<T>>,
    afterHidingLoading: (T?) -> Unit,
    canCancelDialog: Boolean = true,
) {
    lifecycleScope.launch {
        activityViewModel?.globalLoading?.value = true

        delay(150)

        when (val result = afterShowingLoading()) {
            is MAResult.Failure -> {
                Timber.e("failure is $result")

                activityViewModel?.globalLoading?.value = false

                delay(150)

                if (result.failureStatus == MAResult.Failure.Status.TOKEN_EXPIRED) {
                    prefsAccount.logOut()

                    val navController = findNavControllerOfProject()

                    navController.popAllBackStacks()

                    navController.navigateDeepLinkWithoutOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.log.in",
                    )

                    return@launch
                }

                activityViewModel?.globalError?.value = GlobalError.Show(result.message, canCancelDialog)

                activityViewModel?.globalError?.observe(viewLifecycleOwner, object :
                    Observer<GlobalError> {
                    override fun onChanged(globalError: GlobalError?) {
                        if (globalError is GlobalError.Retry) {
                            activityViewModel?.globalError?.removeObserver(this)

                            activityViewModel?.globalError?.value = GlobalError.Cancel

                            Handler(Looper.getMainLooper()).post {
                                executeOnGlobalLoadingAndAutoHandleRetryCancellable(afterShowingLoading, afterHidingLoading, canCancelDialog)
                            }
                        }
                    }
                })
            }
            is MAResult.Success -> {
                activityViewModel?.globalLoading?.value = false

                delay(150)

                afterHidingLoading(result.value.data)
            }
        }
    }
}

fun <T> MABaseFragment<*>.executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
    afterShowingLoading: suspend () -> MAResult.Immediate<MABaseResponse<T>>,
    afterHidingLoading: (MABaseResponse<T>) -> Unit,
    canCancelDialog: Boolean = true,
) {
    lifecycleScope.launch {
        activityViewModel?.globalLoading?.value = true

        delay(150)

        when (val result = afterShowingLoading()) {
            is MAResult.Failure -> {
                Timber.e("failure is $result")

                activityViewModel?.globalLoading?.value = false

                delay(150)

                if (result.failureStatus == MAResult.Failure.Status.TOKEN_EXPIRED) {
                    prefsAccount.logOut()

                    val navController = findNavControllerOfProject()

                    navController.popAllBackStacks()

                    navController.navigateDeepLinkWithoutOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.log.in",
                    )

                    return@launch
                }

                activityViewModel?.globalError?.value = GlobalError.Show(result.message, canCancelDialog)

                activityViewModel?.globalError?.observe(viewLifecycleOwner, object :
                    Observer<GlobalError> {
                    override fun onChanged(globalError: GlobalError?) {
                        if (globalError is GlobalError.Retry) {
                            activityViewModel?.globalError?.removeObserver(this)

                            activityViewModel?.globalError?.value = GlobalError.Cancel

                            Handler(Looper.getMainLooper()).post {
                                executeOnGlobalLoadingAndAutoHandleRetryCancellable2(afterShowingLoading, afterHidingLoading, canCancelDialog)
                            }
                        }
                    }
                })
            }
            is MAResult.Success -> {
                activityViewModel?.globalLoading?.value = false

                delay(150)

                afterHidingLoading(result.value)
            }
        }
    }
}

fun <T> MADialogFragment<*>.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
    afterShowingLoading: suspend () -> MAResult.Immediate<MABaseResponse<T>>,
    afterHidingLoading: (T?) -> Unit,
    canCancelDialog: Boolean = true,
) {
    lifecycleScope.launch {
        activityViewModel?.globalLoading?.value = true

        delay(150)

        when (val result = afterShowingLoading()) {
            is MAResult.Failure -> {
                Timber.e("failure is $result")

                activityViewModel?.globalLoading?.value = false

                delay(150)

                if (result.failureStatus == MAResult.Failure.Status.TOKEN_EXPIRED) {
                    prefsAccount.logOut()

                    val navController = findNavControllerOfProject()

                    navController.popAllBackStacks()

                    navController.navigateDeepLinkWithoutOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.log.in",
                    )

                    return@launch
                }

                activityViewModel?.globalError?.value = GlobalError.Show(result.message, canCancelDialog)

                activityViewModel?.globalError?.observe(viewLifecycleOwner, object :
                    Observer<GlobalError> {
                    override fun onChanged(globalError: GlobalError?) {
                        if (globalError is GlobalError.Retry) {
                            activityViewModel?.globalError?.removeObserver(this)

                            activityViewModel?.globalError?.value = GlobalError.Cancel

                            Handler(Looper.getMainLooper()).post {
                                executeOnGlobalLoadingAndAutoHandleRetryCancellable(afterShowingLoading, afterHidingLoading, canCancelDialog)
                            }
                        }
                    }
                })
            }
            is MAResult.Success -> {
                activityViewModel?.globalLoading?.value = false

                delay(150)

                afterHidingLoading(result.value.data)
            }
        }
    }
}

fun MABaseFragment<*>.executeShowingErrorOnce(
    canCancelDialog: Boolean,
    errorMessage: String,
    onRetryClick: () -> Unit,
) {
    activityViewModel?.globalError?.value = GlobalError.Show(errorMessage, canCancelDialog)

    activityViewModel?.globalError?.observe(viewLifecycleOwner, object :
        Observer<GlobalError> {
        override fun onChanged(globalError: GlobalError?) {
            if (globalError is GlobalError.Retry) {
                activityViewModel?.globalError?.removeObserver(this)

                activityViewModel?.globalError?.value = GlobalError.Cancel

                Handler(Looper.getMainLooper()).post {
                    onRetryClick()
                }
            }
        }
    })
}

fun SharedMainActivity.executeShowingErrorOnce(
    canCancelDialog: Boolean,
    errorMessage: String,
    onRetryClick: () -> Unit,
) {
    viewModel.globalError.value = GlobalError.Show(errorMessage, canCancelDialog)

    viewModel.globalError.observe(this, object :
        Observer<GlobalError> {
        override fun onChanged(globalError: GlobalError?) {
            if (globalError is GlobalError.Retry) {
                viewModel.globalError.removeObserver(this)

                viewModel.globalError.value = GlobalError.Cancel

                Handler(Looper.getMainLooper()).post {
                    onRetryClick()
                }
            }
        }
    })
}
