package com.maproductions.mohamedalaa.shared.presentation.settings.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.map
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.presentation.settings.MSGFormFragment
import com.maproductions.mohamedalaa.shared.presentation.settings.MSGFormFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MSGFormViewModel @Inject constructor(
    val args: MSGFormFragmentArgs,
    prefsAccount: PrefsAccount,
    private val repoSettings: RepoSettings,
    navArgs: NavSharedArgs,
) : ViewModel() {

    private val userData = prefsAccount.getUserData().asLiveData()

    private val providerData = prefsAccount.getProviderData().asLiveData()

    val name = if (navArgs.isUserNotProvider) {
        userData.map { it?.name }
    }else {
        providerData.map { it?.name }
    }

    val email = if (navArgs.isUserNotProvider) {
        userData.map { it?.email }
    }else {
        providerData.map { it?.email }
    }

    val phone = if (navArgs.isUserNotProvider) {
        userData.map { it?.phone }
    }else {
        providerData.map { it?.phone }
    }

    val message = MutableLiveData("")

    fun send(view: View) {
        if (name.value.isNullOrEmpty() || email.value.isNullOrEmpty() ||
            phone.value.isNullOrEmpty() || message.value.isNullOrEmpty()) {
            return view.context.showErrorToast(view.context.getString(R.string.all_fields_required))
        }

        view.findFragment<MSGFormFragment>().executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
            afterShowingLoading = {
                repoSettings.contactUsOrSendComplainsOrSuggestions(
                    name.value.orEmpty(),
                    email.value.orEmpty(),
                    phone.value.orEmpty(),
                    message.value.orEmpty(),
                    args.title == view.context.getString(R.string.technical_support),
                    if (args.orderId == -1) null else args.orderId
                )
            },
            afterHidingLoading = {
                view.context.showSuccessToast(it.message)

                view.findNavController().navigateUp()
            },
        )
    }

}
