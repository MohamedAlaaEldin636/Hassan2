package com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.order.ConfirmFinishingWorkDialogFragmentArgs
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfirmFinishingWorkViewModel @Inject constructor(
    application: Application,
    private val args: ConfirmFinishingWorkDialogFragmentArgs
) : AndroidViewModel(application) {

    fun done(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
            "dialog-dest",
            "com.grand.hassan.shared.money.received.dialog",
            args.orderId.toString(),
            args.amountToPay.toString()
        )
    }

    fun addMore(view: View) {
        val navController = view.findNavControllerOfProject()

        navController.navigateUp()

        navController.navigateDeepLinkWithoutOptions(
            "fragment-dest",
            "com.grand.hassan.shared.adding.services",
            args.orderId.toString(),
            args.categoryId.toString(),
            args.amountToPay.toString(),
            args.orderMinPriceForExtra.toString(),
            args.jsonOfServicesInOrderDetails
        )
    }

}
