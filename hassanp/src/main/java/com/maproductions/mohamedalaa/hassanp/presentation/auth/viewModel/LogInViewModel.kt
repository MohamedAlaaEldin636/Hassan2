package com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanp.presentation.auth.LogInFragment
import com.maproductions.mohamedalaa.hassanp.presentation.auth.LogInFragmentDirections
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsSplash
import com.maproductions.mohamedalaa.shared.domain.local.preferences.UserData
import com.maproductions.mohamedalaa.shared.domain.location.LocationSelectionApiAction
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val repoAuth: RepoAuth,
    private val prefsAccount: PrefsAccount,
    private val prefsSplash: PrefsSplash,
) : ViewModel() {

    val phone = MutableLiveData("")

    val password = MutableLiveData("")

    fun logIn(view: View) {
        if (phone.value.isNullOrEmpty() || password.value.isNullOrEmpty()) {
            return view.context.showErrorToast(view.context.getString(com.maproductions.mohamedalaa.shared.R.string.all_fields_required))
        }

        if (!phone.value.isValidIraqPhoneWithoutPrefix964()) {
            return view.context.showErrorToast(view.context.getString(SR.string.phone_number_is_wrong))
        }

        val fragment = view.findFragment<LogInFragment>()
        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
            afterShowingLoading = {
                repoAuth.loginProvider(phone.value.orEmpty(), password.value.orEmpty())
            },
            afterHidingLoading = { response ->
                val provider = response.data

                if (provider == null) {
                    view.context.showErrorToast(response.message)

                    return@executeOnGlobalLoadingAndAutoHandleRetryCancellable2
                }

                if (provider.isApproved.not()) {
                    view.context.showErrorToast(fragment.getString(SR.string.waiting_for_admin_approval))

                    return@executeOnGlobalLoadingAndAutoHandleRetryCancellable2
                }

                viewModelScope.launch {
                    prefsAccount.setApiBearerToken(provider.apiToken)
                }

                if (provider.latitude.isNullOrEmpty() || provider.longitude.isNullOrEmpty()
                    || provider.address.isNullOrEmpty()) {
                    viewModelScope.launch {
                        prefsAccount.setProviderData(provider)

                        fragment.findNavControllerOfProject().navigateDeepLinkWithOptions(
                            "fragment-dest",
                            "com.grand.hassan.shared.location.selection.back.button.and.api.action",
                            paths = arrayOf(false.toString(), LocationSelectionApiAction.UPDATE_PROVIDER_PROFILE.name)
                        )
                    }
                }else {
                    viewModelScope.launch {
                        prefsAccount.setProviderData(provider)

                        prefsSplash.setInitialLaunch(
                            if (provider.isSuspendedAccount) {
                                SplashInitialLaunch.PROVIDER_ACCOUNT_SUSPENDED
                            }else {
                                SplashInitialLaunch.MAIN
                            }
                        )

                        PusherUtils.loginBeams(
                            false,
                            provider.apiToken,
                            provider.id
                        )

                        fragment.findNavControllerOfProject().navigateDeepLinkWithOptions(
                            "fragment-dest",
                            "com.grand.hassan.shared.provider.bottom.nav.suspend.account",
                            defaultAnimationsNavOptionsBuilder()
                                .setPopUpTo(R.id.dest_log_in, true)
                                .build(),
                            provider.isSuspendedAccount.toString()
                        )
                    }
                }
            }
        )
    }

    fun forgetPassword(view: View) {
        view.findNavControllerOfProject().navigate(
            LogInFragmentDirections.actionDestLogInToDestForgetPassword()
        )
    }

    fun registerAccount(view: View) {
        view.findNavController().navigate(
            LogInFragmentDirections.actionDestLogInToDestRegisterForm()
        )
    }

}
