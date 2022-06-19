package com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.presentation.address.adapters.RVItemAddress
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServicesLocationSelectionFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServicesLocationSelectionFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.RVItemAddressSingleSelection
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.core.extensions.showErrorToast
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServicesLocationSelectionViewModel @Inject constructor(
    repoSettings: RepoSettings,
    private val args: ServicesLocationSelectionFragmentArgs
) : ViewModel() {

    val retryAbleFlow = RetryAbleFlow(repoSettings::getUserAddresses)

    val adapter = RVItemAddressSingleSelection()

    fun addNewAddress(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.location.selection.no.args",
        )
    }

    fun next(view: View) {
        val item = adapter.getSelectedItemOrNull() ?: return view.context.showErrorToast(
            view.context.getString(SR.string.pick_address)
        )

        view.findNavController().navigate(
            ServicesLocationSelectionFragmentDirections.actionDestServicesLocationSelectionToDestServiceDateAndTimeSelection(
                args.categoryId,
                args.categoryName,
                args.jsonOfServices,
                item.id,
                args.jsonDeliveryData
            )
        )
    }

}
