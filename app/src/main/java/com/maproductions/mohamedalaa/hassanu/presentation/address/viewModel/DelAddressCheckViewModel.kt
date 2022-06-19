package com.maproductions.mohamedalaa.hassanu.presentation.address.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.presentation.address.AddNewAddressFragment
import com.maproductions.mohamedalaa.hassanu.presentation.address.DelAddressCheckDialogFragment
import com.maproductions.mohamedalaa.hassanu.presentation.address.DelAddressCheckDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.address.MyAddressesFragment
import com.maproductions.mohamedalaa.shared.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DelAddressCheckViewModel @Inject constructor(
    application: Application,
    private val args: DelAddressCheckDialogFragmentArgs,
    private val repoSettings: RepoSettings,
) : AndroidViewModel(application) {

    fun delAddress(view: View) {
        val fragment = view.findFragment<DelAddressCheckDialogFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoSettings.deleteUserAddress(args.id)
            },
            afterHidingLoading = {
                Timber.e("abcdef DelAddressCheckViewModel id ${args.id}")

                val navController = fragment.findNavController()

                navController.navigateUp()

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    DelAddressCheckDialogFragment.KEY_FRAGMENT_DELETION_ID,
                    args.id
                )
            }
        )
    }

    fun goBack(view: View) {
        view.findFragment<DelAddressCheckDialogFragment>().findNavController().navigateUp()
    }

}
