package com.maproductions.mohamedalaa.shared.presentation.settings.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.domain.settings.ImageWithTextAndTitleFlag
import com.maproductions.mohamedalaa.shared.presentation.settings.ImageWithTextAndTitleFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageWithTextAndTitleViewModel @Inject constructor(
    application: Application,
    args: ImageWithTextAndTitleFragmentArgs,
    repoSettings: RepoSettings
) : AndroidViewModel(application) {

    val retryAbleFlow = RetryAbleFlow {
        when(args.flag) {
            ImageWithTextAndTitleFlag.USER_TERMS_AND_CONDITIONS -> repoSettings.getTermsAndConditionsFor(true)
            ImageWithTextAndTitleFlag.PROVIDER_TERMS_AND_CONDITIONS -> repoSettings.getTermsAndConditionsFor(false)
            ImageWithTextAndTitleFlag.USER_ABOUT -> repoSettings.getAboutAppFor(true)
        }
    }

    val title = when(args.flag) {
        ImageWithTextAndTitleFlag.USER_TERMS_AND_CONDITIONS, ImageWithTextAndTitleFlag.PROVIDER_TERMS_AND_CONDITIONS -> myApp.getString(R.string.terms_and_conditions)
        ImageWithTextAndTitleFlag.USER_ABOUT -> myApp.getString(R.string.about_app)
    }

    val imageUrl = MutableLiveData("")

    val textAsHtml = MutableLiveData("")

    fun goBack(view: View) {
        view.findNavControllerOfProject().navigateUp()
    }

}