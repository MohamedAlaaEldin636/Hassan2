package com.maproductions.mohamedalaa.hassanu.presentation.auth.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.presentation.auth.LogInFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.auth.RegisterPhoneFragment
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.presentation.auth.RegisterPhoneFragmentArgs
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsSplash
import com.maproductions.mohamedalaa.shared.domain.local.preferences.UserData
import com.maproductions.mohamedalaa.shared.presentation.auth.VerifyPhoneFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterPhoneViewModel @Inject constructor(
    private val repoAuth: RepoAuth,
    private val gson: Gson,
    private val args: RegisterPhoneFragmentArgs,
    private val prefsAccount: PrefsAccount,
) : ViewModel() {

    val phone = MutableLiveData("")

    fun onConfirmClick(view: View) {
        if (phone.value.isNullOrEmpty()) {
            return view.context.showErrorToast(view.context.getString(SR.string.field_required))
        }

        if (!phone.value.isValidIraqPhoneWithoutPrefix964()) {
            return view.context.showErrorToast(view.context.getString(SR.string.phone_number_is_wrong))
        }

        val fragment = view.findFragment<RegisterPhoneFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoAuth.socialLogin(args.socialId, phone.valueNotNull)
            },
            afterHidingLoading = {
                Timber.e("heeey !args.name.isNullOrEmpty() ${!args.name.isNullOrEmpty()}")

                if (!args.name.isNullOrEmpty()) {
                    fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
                        afterShowingLoading = {
                            prefsAccount.setApiBearerToken(it?.apiToken.orEmpty())

                            repoAuth.updateUserProfile(args.name.orEmpty(), args.email)
                        },
                        afterHidingLoading = { _ ->
                            val navController = fragment.findNavControllerOfProject()

                            if (it == null || !it.isVerified) {
                                navController.navigateUp()

                                navController.navigateDeepLinkWithOptions(
                                    "fragment-dest",
                                    "com.grand.hassan.shared.verify.phone",
                                    paths = arrayOf(true.toString(), phone.value.orEmpty())
                                )
                            }else {
                                viewModelScope.launch {
                                    prefsAccount.setApiBearerToken(it.apiToken.orEmpty())

                                    navController.navigateUp()

                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        VerifyPhoneFragment.SAVED_STATE_VERIFICATION_RESPONSE_AS_JSON,
                                        it.toJson(gson)
                                    )
                                }
                            }
                        }
                    )
                }else {
                    val navController = fragment.findNavControllerOfProject()

                    if (it == null || !it.isVerified) {
                        navController.navigateUp()

                        navController.navigateDeepLinkWithOptions(
                            "fragment-dest",
                            "com.grand.hassan.shared.verify.phone",
                            paths = arrayOf(true.toString(), phone.value.orEmpty())
                        )
                    }else {
                        viewModelScope.launch {
                            prefsAccount.setApiBearerToken(it.apiToken.orEmpty())

                            navController.navigateUp()

                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                VerifyPhoneFragment.SAVED_STATE_VERIFICATION_RESPONSE_AS_JSON,
                                it.toJson(gson)
                            )
                        }
                    }
                }
            }
        )
    }

}
