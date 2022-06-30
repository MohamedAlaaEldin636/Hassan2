package com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.maproductions.mohamedalaa.hassanp.presentation.auth.ForgetPasswordFragment
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val repoAuth: RepoAuth,
) : ViewModel()  {

    val phone = MutableLiveData("")

    fun send(view: View) {
        if (phone.value.isNullOrEmpty()) {
            return view.context.showErrorToast(view.context.getString(R.string.something_went_wrong))
        }

        if (!phone.value.isValidIraqPhoneWithoutPrefix964()) {
            return view.context.showErrorToast(view.context.getString(R.string.phone_number_is_wrong))
        }

        val fragment = view.findFragment<ForgetPasswordFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoAuth.resendCode(phone.value.orEmpty(), false)
            },
            afterHidingLoading = {
                view.findNavController().navigateDeepLinkWithOptions(
                    "fragment-dest",
                    "com.grand.hassan.shared.verify.phone.is.forget.password",
                    paths = arrayOf(false.toString(), phone.value.orEmpty(), true.toString())
                )
            }
        )
    }

    fun rememberedPassword(view: View) {
        view.findNavControllerOfProject().navigateUp()
    }

}
