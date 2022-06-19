package com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.presentation.home.NewOrderDialogFragment
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanp.presentation.home.NewOrderDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanp.presentation.order.OrderDetailsFragment
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.domain.pusher.ResponsePusherOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewOrderViewModel @Inject constructor(
    application: Application,
    args: NewOrderDialogFragmentArgs,
    gson: Gson,
    private val repoOrder: RepoOrder,
) : AndroidViewModel(application) {

    private val order = ResponsePusherOrder.fromJsonOrNull(args.jsonResponsePusherOrder, gson)?.order
    //private val order = args.jsonResponsePusherOrder.fromJsonOrNull<ResponsePusherOrder>(gson)?.order

    val image = order?.user?.image

    val name = order?.user?.name

    val address = order?.address?.address

    val timeAndDate = order?.orderedAt

    val totalPrice by lazy {
        "${order?.total.orZero()} ${myApp.getString(SR.string.egp)}"
    }

    val services = order?.services.orEmpty()

    fun reject(view: View) {
        val fragment = view.findFragment<NewOrderDialogFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoOrder.rejectOrder(order?.id.orZero())
            },
            afterHidingLoading = {
                val navController = fragment.findNavControllerOfProject()

                navController.navigateUp()

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    OrderDetailsFragment.SAVED_STATE_ORDER_DETAILS_MADE_CHANGE,
                    true
                )
            }
        )
    }

    fun accept(view: View) {
        val fragment = view.findFragment<NewOrderDialogFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoOrder.acceptOrder(order?.id.orZero())
            },
            afterHidingLoading = {
                val navController = fragment.findNavControllerOfProject()

                navController.navigateUp()

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    OrderDetailsFragment.SAVED_STATE_ORDER_DETAILS_MADE_CHANGE,
                    true
                )
            }
        )
    }

    fun showDetails(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.order.details",
            paths = arrayOf(order?.id?.orZero()?.toString() ?: return)
        )
    }

}
