package com.maproductions.mohamedalaa.shared.presentation.auth.viewModel

import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.presentation.auth.VerifyPhoneFragment
import com.maproductions.mohamedalaa.shared.presentation.auth.VerifyPhoneFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VerifyPhoneViewModel @Inject constructor(
    private val repoAuth: RepoAuth,
    val args: VerifyPhoneFragmentArgs,
    private val prefsAccount: PrefsAccount,
    private val navSharedArgs: NavSharedArgs,
    private val gson: Gson,
) : ViewModel() {

    private val timer = object : CountDownTimer(60_000L, 1_000L) {
        override fun onTick(millisUntilFinished: Long) {
            val seconds = millisUntilFinished / 1000

            val suffixString = if (seconds < 10) "0$seconds" else "$seconds"

            countDownText.value = "00 : $suffixString"
        }

        override fun onFinish() {
            canResend.value = true
            countDownText.value = "00 : 00"
        }
    }

    val canResend = MutableLiveData(false)

    val countDownText = MutableLiveData("00 : 60")

    val firstConfirmText = MutableLiveData("")
    val secondConfirmText = MutableLiveData("")
    val thirdConfirmText = MutableLiveData("")
    val fourthConfirmText = MutableLiveData("")

    val code = MutableLiveData("")

    init {
        timer.start()
    }

    fun onResendClick(view: View) {
        view.findFragment<VerifyPhoneFragment>().executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
            afterShowingLoading = {
                repoAuth.resendCode(args.phone, navSharedArgs.isUserNotProvider)
            },
            afterHidingLoading = {
                view.context.showSuccessToast(it.message)

                timer.start()
            }
        )
    }

    fun onConfirmClick(view: View) {
        val code = firstConfirmText.value.orEmpty() +
            secondConfirmText.value.orEmpty() +
            thirdConfirmText.value.orEmpty() +
            fourthConfirmText.value.orEmpty()

        if (code.length != 4) {
            return view.context.showErrorToast(view.context.getString(R.string.all_fields_required))
        }

        val fragment = view.findFragment<VerifyPhoneFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoAuth.verifyCode(args.phone, code, navSharedArgs.isUserNotProvider)
            },
            afterHidingLoading = {
                if (it == null || it.apiToken.orEmpty().isEmpty()) {
                    view.context?.showErrorToast(fragment.getString(R.string.something_went_wrong))

                    return@executeOnGlobalLoadingAndAutoHandleRetryCancellable
                }

                viewModelScope.launch {
                    prefsAccount.setApiBearerToken(it.apiToken.orEmpty())

                    timer.cancel()

                    val navController = view.findNavController()

                    navController.navigateUp()

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        VerifyPhoneFragment.SAVED_STATE_VERIFICATION_RESPONSE_AS_JSON,
                        it.toJson(gson)
                    )
                }
            }
        )
    }

    override fun onCleared() {
        timer.cancel()

        super.onCleared()
    }

}
