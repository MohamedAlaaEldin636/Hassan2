package com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maproductions.mohamedalaa.hassanu.presentation.order.OrderAdditionsFragment
import com.maproductions.mohamedalaa.hassanu.presentation.order.OrderAdditionsFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.order.OrderAdditionsFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.RVItemServiceNameAndPrice
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrderDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderAdditionsViewModel @Inject constructor(
    application: Application,
    private val repoOrder: RepoOrder,
    private val args: OrderAdditionsFragmentArgs
) : AndroidViewModel(application) {

    val retryAbleOrderDetails = RetryAbleFlow {
        repoOrder.getAdditionalServices(args.orderId)
    }

    val adapterServicesDetails = RVItemServiceNameAndPrice(emptyList(), false)

    val adapterServicesSummary = RVItemServiceNameAndPrice(emptyList(), true)

    val amountToBePaid = MutableLiveData("")

    fun confirm(view: View) {
        val fragment = view.findFragment<OrderAdditionsFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoOrder.finishOrderByUserByAccepting(args.orderId)
            },
            afterHidingLoading = {
                val navController = fragment.findNavControllerOfProject()

                navController.navigateUp()
                navController.navigateUp()
            }
        )
    }

    fun doNotAdd(view: View) {
        view.findNavControllerOfProject().navigate(
            OrderAdditionsFragmentDirections.actionDestOrderAdditionsToDestFinishedOrderPaymentDialog(
                args.orderId
            )
        )
    }

    fun makeComplaint(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.msg.form.with.order.id",
            paths = arrayOf(
                view.context.getString(R.string.complains_or_suggestions),
                args.orderId.toString()
            )
        )
    }

}
