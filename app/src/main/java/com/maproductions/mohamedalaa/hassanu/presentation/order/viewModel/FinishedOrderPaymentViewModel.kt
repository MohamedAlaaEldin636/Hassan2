package com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maproductions.mohamedalaa.hassanu.presentation.order.FinishedOrderPaymentDialogFragment
import com.maproductions.mohamedalaa.hassanu.presentation.order.FinishedOrderPaymentDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.order.OrderAdditionsFragment
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import com.maproductions.mohamedalaa.shared.core.extensions.showErrorToast
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinishedOrderPaymentViewModel @Inject constructor(
    application: Application,
    private val args: FinishedOrderPaymentDialogFragmentArgs,
    private val repoOrder: RepoOrder,
) : AndroidViewModel(application) {

    val moneyYouPaid = MutableLiveData("")

    fun send(view: View) {
        if (moneyYouPaid.value.isNullOrEmpty() || moneyYouPaid.value.orEmpty().toFloatOrNull() == null) {
            return view.context.showErrorToast(view.context.getString(R.string.field_required))
        }

        val fragment = view.findFragment<FinishedOrderPaymentDialogFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoOrder.finishOrderByUserByRejecting(args.orderId, moneyYouPaid.value.orEmpty().toFloat())
            },
            afterHidingLoading = {
                val navController = fragment.findNavControllerOfProject()

                navController.navigateUp()
                navController.navigateUp()
                navController.navigateUp()
            }
        )
    }

}
