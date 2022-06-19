package com.maproductions.mohamedalaa.hassanu.presentation.settings.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.presentation.bottomNav.UserBottomNavFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.settings.SettingsFragmentArgs
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.domain.settings.ImageWithTextAndTitleFlag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val args: SettingsFragmentArgs,
    private val prefsAccount: PrefsAccount,
) : AndroidViewModel(application) {

    private fun View.launchRequiresLoginDialogIfGuestOrElseRunBlock(block: () -> Unit) {
        if (args.isGuest) {
            findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                "fragment-dest",
                "com.grand.hassan.shared.guest.please.login.dialog"
            )
        }else {
            block()
        }
    }

    fun toPersonalData(view: View) {
        view.launchRequiresLoginDialogIfGuestOrElseRunBlock {
            view.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.user.personal.data"
            )
        }
    }

    fun toGetDiscounts(view: View) {
        view.launchRequiresLoginDialogIfGuestOrElseRunBlock {
            view.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.user.get.discounts"
            )
        }
    }

    fun toRegisterAsProvider(view: View) {
        view.findNavControllerOfProject().navigate(
            UserBottomNavFragmentDirections.actionDestUserBottomNavToDestMoveToProviderAppDialog()
        )
    }

    fun toWallet(view: View) {
        view.launchRequiresLoginDialogIfGuestOrElseRunBlock {
            view.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.user.wallet"
            )
        }
    }

    fun toMyAddresses(view: View) {
        view.launchRequiresLoginDialogIfGuestOrElseRunBlock {
            view.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.user.my.addresses"
            )
        }
    }

    fun toDiscountCodes(view: View) {
        view.launchRequiresLoginDialogIfGuestOrElseRunBlock {
            view.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.user.codes.of.discounts"
            )
        }
    }

    fun toAboutApp(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.image.with.text.and.title",
            paths = arrayOf(ImageWithTextAndTitleFlag.USER_ABOUT.name)
        )
    }

    fun toTechnicalSupport(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.msg.form",
            paths = arrayOf(view.context.getString(SR.string.technical_support))
        )
    }

    fun toComplainsAndSuggestions(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.msg.form",
            paths = arrayOf(view.context.getString(SR.string.complains_or_suggestions))
        )
    }

    fun shareApp(view: View) {
        view.context.apply {
            launchShareText(
                "${getString(R.string.app_name)}\n${getAppWebLinkOnGooglePay()}"
            )
        }
    }

    fun reviewApp(view: View) {
        view.context.launchAppOnGooglePlay()
    }

    fun logOut(view: View) {
        view.launchRequiresLoginDialogIfGuestOrElseRunBlock {
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

    fun toTermsAndConditions(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.image.with.text.and.title",
            paths = arrayOf(ImageWithTextAndTitleFlag.USER_TERMS_AND_CONDITIONS.name)
        )
    }

}
