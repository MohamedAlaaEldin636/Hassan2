package com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.presentation.service.AcceptedProviderServiceRequestDialogFragmentArgs
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AcceptedProviderServiceRequestViewModel @Inject constructor(
    application: Application,
    private val args: AcceptedProviderServiceRequestDialogFragmentArgs,
) : AndroidViewModel(application) {

    val text by lazy {
        myApp.getString(SR.string.var_accepted_your_request, args.providerName)
    }

    fun showRequestDetails(view: View) {
        val navController = view.findNavControllerOfProject()

        navController.navigateUp()

        navController.navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.order.details",
            paths = arrayOf(args.orderId.toString())
        )
    }

}
