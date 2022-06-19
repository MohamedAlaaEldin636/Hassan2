package com.maproductions.mohamedalaa.hassanu.presentation.auth.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.presentation.auth.RegisterPersonalDataFragment
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.core.extensions.showErrorToast
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsSplash
import com.maproductions.mohamedalaa.shared.domain.local.preferences.UserData
import com.maproductions.mohamedalaa.shared.domain.location.LocationSelectionApiAction
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterPersonalDataViewModel @Inject constructor(
    private val repoAuth: RepoAuth,
    private val prefsSplash: PrefsSplash,
    private val prefsAccount: PrefsAccount,
) : ViewModel() {

    val name = MutableLiveData("")

    val email = MutableLiveData("")

    fun onConfirmClick(view: View) {
        if (name.value.isNullOrEmpty()) {
            return view.context.run {
                showErrorToast(getString(SR.string.user_name_required))
            }
        }

        val fragment = view.findFragment<RegisterPersonalDataFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            canCancelDialog = true,
            afterShowingLoading = {
                repoAuth.updateUserProfile(name.value.orEmpty(), email.value)
            },
            afterHidingLoading = {
                viewModelScope.launch {
                    prefsAccount.setUserData(
                        prefsAccount.getUserData().first()!!.copy(
                            id = it?.id ?: -1,
                            name = name.value.orEmpty(),
                            email = email.value.orEmpty(),
                        )
                    )

                    prefsSplash.setInitialLaunch(SplashInitialLaunch.LOGIN_LOCATION)

                    goToLocationSelection(fragment)
                }
            }
        )
    }

    fun goToLocationSelection(fragment: RegisterPersonalDataFragment) {
        fragment.findNavController().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.location.selection.back.button.and.api.action",
            paths = arrayOf(false.toString(), LocationSelectionApiAction.UPDATE_USER_PROFILE.name)
        )
    }

}
