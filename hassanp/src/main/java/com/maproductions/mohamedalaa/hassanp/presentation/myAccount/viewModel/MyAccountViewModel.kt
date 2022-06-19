package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import com.maproductions.mohamedalaa.shared.core.extensions.popAllBackStacks
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    application: Application,
    private val prefsAccount: PrefsAccount,
) : AndroidViewModel(application) {

    fun toPersonalData(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.personal.data"
        )
    }

    fun toMyPreviousWorks(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.previous.works"
        )
    }

    fun toSpecializationAndServices(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.specializations.and.services"
        )
    }

    fun toWallet(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.wallet"
        )
    }

    fun toRatings(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.my.reviews"
        )
    }

    fun logOut(view: View) {
        viewModelScope.launch {
            prefsAccount.logOut()

            val navController = view.findNavControllerOfProject()

            navController.popAllBackStacks()

            navController.navigateDeepLinkWithoutOptions(
                "fragment-dest",
                "com.grand.hassan.shared.log.in",
            )
        }
    }

}