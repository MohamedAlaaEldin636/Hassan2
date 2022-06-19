package com.maproductions.mohamedalaa.hassanp.presentation.more.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.hassanp.R as AppR
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.settings.ImageWithTextAndTitleFlag
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

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
            paths = arrayOf(view.context.getString(R.string.technical_support))
        )
    }

    fun toComplainsOrSuggestions(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.msg.form",
            paths = arrayOf(view.context.getString(R.string.complains_or_suggestions))
        )
    }

    fun shareApp(view: View) {
        view.context.apply {
            launchShareText(
                "${getString(AppR.string.app_name)}\n${getAppWebLinkOnGooglePay()}"
            )
        }
    }

    fun rateApp(view: View) {
        view.context.launchAppOnGooglePlay()
    }

    fun toTermsAndConditions(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.image.with.text.and.title",
            paths = arrayOf(ImageWithTextAndTitleFlag.PROVIDER_TERMS_AND_CONDITIONS.name)
        )
    }

}
