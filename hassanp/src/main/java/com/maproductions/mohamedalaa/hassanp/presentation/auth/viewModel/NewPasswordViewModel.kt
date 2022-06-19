package com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.auth.NewPasswordFragment
import com.maproductions.mohamedalaa.hassanp.presentation.auth.NewPasswordFragmentArgs
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val args: NewPasswordFragmentArgs,
    private val repoAuth: RepoAuth,
) : ViewModel()  {

    val password = MutableLiveData("")

    val confirmPassword = MutableLiveData("")

    fun confirm(view: View) {
        if (password.value.isNullOrEmpty() || confirmPassword.value.isNullOrEmpty()) {
            return view.context.showErrorToast(view.context.getString(R.string.all_fields_required))
        }

        if (password.value != confirmPassword.value) {
            return view.context.showErrorToast(view.context.getString(R.string.password_not_same_as_confirm_password))
        }

        val fragment = view.findFragment<NewPasswordFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
            afterShowingLoading = {
                repoAuth.forgetPassword(args.phone, password.value.orEmpty())
            },
            afterHidingLoading = {
                fragment.context?.showSuccessToast(it.message)

                val navController = fragment.findNavControllerOfProject()

                navController.navigateUp()
                navController.navigateUp()
            }
        )
    }

}
