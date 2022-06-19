package com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel

import android.app.Application
import android.location.Location
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.maproductions.mohamedalaa.hassanp.presentation.order.OrderDetailsFragment
import com.maproductions.mohamedalaa.hassanp.presentation.order.OrderDetailsFragmentArgs
import com.maproductions.mohamedalaa.hassanp.presentation.order.adapter.RVItemServiceNameAndPrice
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.hassanp.R as SR
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.customTypes.MAResult
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrderDetails
import com.maproductions.mohamedalaa.shared.presentation.base.adapters.RVItemImageRect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    application: Application,
    private val repoOrder: RepoOrder,
    val args: OrderDetailsFragmentArgs
) : AndroidViewModel(application) {

    val retryAbleOrderDetails = RetryAbleFlow {
        repoOrder.getOrderDetails(args.orderId)
    }

    var location: Location? = null

    val canPressOnArrived = MutableLiveData(false)

    val orderDetails = MutableLiveData<ResponseOrderDetails>()

    val orderStatus = orderDetails.map { it?.statusOfOrder ?: ApiOrderStatus.PENDING }

    val statusDrawableRes = orderStatus.map {
        when (it) {
            ApiOrderStatus.PENDING, ApiOrderStatus.CANCELLED, ApiOrderStatus.REJECTED -> 0
            ApiOrderStatus.ACCEPTED -> R.drawable.ic_request_is_received
            ApiOrderStatus.ON_THE_WAY -> R.drawable.ic_on_the_way
            ApiOrderStatus.ARRIVED -> R.drawable.ic_arrived
            ApiOrderStatus.WORK_STARTED -> R.drawable.ic_start_working
            ApiOrderStatus.FINISHED -> R.drawable.ic_finished_working
        }
    }

    val adapterImages = RVItemImageRect(false)
    val adapterServicesDetails = RVItemServiceNameAndPrice(emptyList(), false)
    val adapterServicesSummary = RVItemServiceNameAndPrice(emptyList(), true)

    val showImages = MutableLiveData(false)

    val providerImageUrl = orderDetails.map { it?.user?.imageUrl.orEmpty() }
    val providerName = orderDetails.map { it?.user?.name.orEmpty() }

    val addressTitle = orderDetails.map { it?.address?.title.orEmpty() }
    val addressDetails = orderDetails.map { it?.address?.address.orEmpty() }

    val date = orderDetails.map { it?.orderedAtDate.orEmpty() }
    val time = orderDetails.map { it?.orderedAtTime.orEmpty() }

    val description = orderDetails.map { it?.extraNotes.orEmpty() }

    val servicesTitleDrawableRes = orderStatus.map {
        when (it) {
            ApiOrderStatus.PENDING, ApiOrderStatus.FINISHED -> R.drawable.ic_services_agreed_up_on_black
            else -> R.drawable.ic_services_agreed_up_on
        }
    }

    val locationTitleDrawableRes = orderStatus.map {
        when (it) {
            ApiOrderStatus.PENDING, ApiOrderStatus.CANCELLED, ApiOrderStatus.REJECTED-> R.drawable.ic_location_yellow
            ApiOrderStatus.FINISHED -> R.drawable.ic_location_yellow_black
            else -> com.maproductions.mohamedalaa.hassanp.R.drawable.ic_yellow_dot_black
        }
    }

    val timeTitleDrawableRes = orderStatus.map {
        when (it) {
            ApiOrderStatus.FINISHED -> R.drawable.ic_calendar_black_full
            else -> R.drawable.ic_calendar_black_portion
        }
    }
    val timeTitleText = orderStatus.map {
        when (it) {
            ApiOrderStatus.FINISHED -> myApp.getString(R.string.time_of_service_execution)
            else -> myApp.getString(R.string.approximate_time_for_arrival)
        }
    }

    val clientImageUrl = orderDetails.map { it?.provider?.imageUrl.orEmpty() }
    val clientName = orderDetails.map { it?.provider?.name.orEmpty() }
    val clientRatingAsPercent = orderDetails.map {
        it?.provider?.let { provider ->
            (provider.averageRate.orZero() * 20f).roundToInt()
        }.orZero()
    }
    val clientRateDescription = orderDetails.map {
        it?.provider?.let { provider ->
            "(${provider.averageRate.orZero().roundHalfUpToIntOrFloat(1)})"
        }.orEmpty()
    }

    val canCallOrChatProvider = orderStatus.map {
        it != ApiOrderStatus.FINISHED && it != ApiOrderStatus.CANCELLED && it != ApiOrderStatus.PENDING
    }

    val firstButtonText = orderStatus.map {
        when (it) {
            ApiOrderStatus.CANCELLED, ApiOrderStatus.REJECTED, ApiOrderStatus.FINISHED -> ""
            ApiOrderStatus.PENDING -> myApp.getString(SR.string.accept_order)
            ApiOrderStatus.ACCEPTED -> ""
            ApiOrderStatus.ON_THE_WAY -> myApp.getString(R.string.arrival_is_done)
            ApiOrderStatus.ARRIVED -> myApp.getString(R.string.start_working)
            ApiOrderStatus.WORK_STARTED -> ""
        }
    }
    val secondButtonText = orderStatus.map {
        when (it) {
            ApiOrderStatus.CANCELLED, ApiOrderStatus.REJECTED, ApiOrderStatus.FINISHED -> ""
            ApiOrderStatus.PENDING -> myApp.getString(SR.string.reject)
            ApiOrderStatus.ACCEPTED -> ""
            ApiOrderStatus.ON_THE_WAY, ApiOrderStatus.ARRIVED -> myApp.getString(R.string.cancel_order)
            ApiOrderStatus.WORK_STARTED -> ""
        }
    }
    val singleButtonText = orderStatus.map {
        when (it) {
            ApiOrderStatus.CANCELLED, ApiOrderStatus.REJECTED, ApiOrderStatus.FINISHED -> ""
            ApiOrderStatus.PENDING -> ""
            ApiOrderStatus.ACCEPTED -> myApp.getString(R.string.cancel_order)
            ApiOrderStatus.ON_THE_WAY -> ""
            ApiOrderStatus.ARRIVED -> ""
            ApiOrderStatus.WORK_STARTED -> myApp.getString(R.string.finished_working)
        }
    }
    /** `null` means show none of these buttons isa. */
    val showSingleButtonNotTwoButtons = orderStatus.map {
        when (it) {
            ApiOrderStatus.CANCELLED, ApiOrderStatus.REJECTED, ApiOrderStatus.FINISHED -> null
            ApiOrderStatus.PENDING -> false
            ApiOrderStatus.ACCEPTED -> true
            ApiOrderStatus.ON_THE_WAY -> false
            ApiOrderStatus.ARRIVED -> false
            ApiOrderStatus.WORK_STARTED -> true
        }
    }

    fun chatWithProvider(view: View) {
        val id = orderDetails.value?.user?.id ?: return
        val orderId = orderDetails.value?.id ?: return
        val imageUrl = orderDetails.value?.user?.imageUrl
        val otherName = orderDetails.value?.user?.name ?: return

        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.chat.details",
            paths = arrayOf(id.toString(), orderId.toString())
        )
    }
    fun callProvider(view: View) {
        view.context.launchDialNumber(orderDetails.value?.user?.phone.orEmpty())
    }

    fun firstButtonClick(view: View) {
        val id = orderDetails.value?.id ?: return

        when (orderStatus.value) {
            ApiOrderStatus.PENDING -> view.changeOrderStatus { repoOrder.acceptOrder(id) }
            ApiOrderStatus.ON_THE_WAY -> view.changeOrderStatus {
                repoOrder.changeOrderStatus(id, ApiOrderStatus.ARRIVED)
            }
            ApiOrderStatus.ARRIVED -> view.changeOrderStatus {
                repoOrder.changeOrderStatus(id, ApiOrderStatus.WORK_STARTED)
            }
            else -> return
        }
    }

    fun secondButtonClick(view: View) {
        val id = orderDetails.value?.id ?: return

        when (orderStatus.value) {
            ApiOrderStatus.PENDING -> view.changeOrderStatus { repoOrder.rejectOrder(id) }
            ApiOrderStatus.ON_THE_WAY, ApiOrderStatus.ARRIVED -> cancelOrder(view)
            else -> return
        }
    }

    fun singleButtonClick(view: View) {
        when (orderStatus.value) {
            ApiOrderStatus.ACCEPTED -> cancelOrder(view)
            ApiOrderStatus.WORK_STARTED -> {
                val amountToPay = adapterServicesSummary.currentList.lastOrNull()?.serviceInCategory
                    ?.price?.roundHalfUpToIntOrFloat(1)?.toFloat() ?: return

                view.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                    "dialog-dest",
                    "com.grand.hassan.shared.confirm.finishing.work.dialog",
                    orderDetails.value?.id?.toString() ?: return,
                    orderDetails.value?.services?.firstOrNull()?.categoryId?.toString() ?: return,
                    amountToPay.toString(),
                    orderDetails.value?.orderMinPriceForExtra.orZero().toString()
                )
            }
            else -> return
        }
    }

    private fun cancelOrder(view: View) {
        val details = orderDetails.value ?: return

        if (orderStatus.value == ApiOrderStatus.ACCEPTED) {
            view.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                "dialog-dest",
                "com.grand.hassan.shared.cancel.order.dialog",
                details.id.toString(),
                myApp.getString(R.string.cancel_order_confirmation_warning_1)
            )
        }else {
            view.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                "dialog-dest",
                "com.grand.hassan.shared.cancellation.reason.dialog",
                details.id.toString(),
                details.cancellationFeesPercent.toString()
            )
        }
    }

    fun showOnMap(view: View) {
        val (latitude, longitude) = orderDetails.value?.address?.let {
            it.latitude to it.longitude
        } ?: return

        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.location_viewer",
            paths = arrayOf(latitude.toString(), longitude.toString()),
        )
    }

    val makePusherSuspendWork = MutableStateFlow(false)

    private fun View.changeOrderStatus(apiFun: suspend () -> MAResult.Immediate<MABaseResponse<Any>>) {
        val fragment = findFragment<OrderDetailsFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
            afterShowingLoading = {
                makePusherSuspendWork.value = true

                apiFun()
            },
            afterHidingLoading = {
                if (orderStatus.value == ApiOrderStatus.ON_THE_WAY) {
                    fragment.locationHandler.stopLocationUpdates()
                }

                retryAbleOrderDetails.retry()

                fragment.getOrderDetails()
            }
        )
    }

}
