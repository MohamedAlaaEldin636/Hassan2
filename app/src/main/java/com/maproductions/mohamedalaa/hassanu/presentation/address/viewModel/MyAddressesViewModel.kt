package com.maproductions.mohamedalaa.hassanu.presentation.address.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.hassanu.presentation.address.MyAddressesFragment
import com.maproductions.mohamedalaa.hassanu.presentation.address.MyAddressesFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.address.adapters.RVItemAddress
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyAddressesViewModel @Inject constructor(
    private val repoSettings: RepoSettings,
) : ViewModel(), RVItemAddress.Listener {

    val retryAbleFlow = RetryAbleFlow(repoSettings::getUserAddresses)

    val adapter = RVItemAddress(this)

    fun addNewAddress(view: View) {
        view.findNavController().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.location.selection.no.args",
        )
    }

    override fun deleteAddress(view: View, id: Int) {
        Timber.e("abcdef MyAddressesViewModel deleteAddress id $id")

        //            app:uri="fragment-dest://com.grand.hassan.user.del.address.check.dialog/{id}"/>
        view.findNavController().navigateDeepLinkWithoutOptions(
            "fragment-dest",
            "com.grand.hassan.user.del.address.check.dialog",
            id.toString()
        )
        // on resume make get instead of get live data isa.
        /*view.findNavController().navigate(
            MyAddressesFragmentDirections.actionDestMyAddressesToDestDelAddressCheckDialog(id)
        )*/
    }

}
