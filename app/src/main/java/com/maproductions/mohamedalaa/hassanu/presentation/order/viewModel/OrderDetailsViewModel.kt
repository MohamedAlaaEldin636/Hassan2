package com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.presentation.order.OrderDetailsFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.order.OrderDetailsFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.RVItemServiceNameAndPrice
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrderDetails
import com.maproductions.mohamedalaa.shared.presentation.base.adapters.RVItemImageRect
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    application: Application,
    val args: OrderDetailsFragmentArgs,
    repoOrder: RepoOrder,
    private val gson: Gson,
) : AndroidViewModel(application) {

    val retryAbleOrderDetails = RetryAbleFlow {
        repoOrder.getOrderDetails(args.id)
    }

    val orderDetails = MutableLiveData<ResponseOrderDetails>()

    val orderStatus = orderDetails.map { it?.statusOfOrder ?: ApiOrderStatus.PENDING }

    val statusDrawableRes = orderStatus.map {
        when (it) {
            ApiOrderStatus.PENDING, ApiOrderStatus.CANCELLED, ApiOrderStatus.REJECTED -> 0
            ApiOrderStatus.ACCEPTED -> R.drawable.ic_request_is_received
            ApiOrderStatus.ON_THE_WAY -> R.drawable.ic_on_the_way
            ApiOrderStatus.ARRIVED -> R.drawable.ic_good_1
            ApiOrderStatus.WORK_STARTED -> R.drawable.ic_start_working
            ApiOrderStatus.FINISHED -> R.drawable.ic_finished_working
        }
    }

    val adapterImages = RVItemImageRect(false)
    val adapterServicesDetails = RVItemServiceNameAndPrice(emptyList(), false)
    val adapterServicesSummary = RVItemServiceNameAndPrice(emptyList(), true)

    val showImages = MutableLiveData(false)

    val providerImageUrl = orderDetails.map { it?.provider?.imageUrl.orEmpty() }
    val providerName = orderDetails.map { it?.provider?.name.orEmpty() }
    val providerRateProgressOfHundred = orderDetails.map {
        it?.provider?.let { provider ->
            (provider.averageRate.orZero() * 20f).roundToInt()
        }.orZero()
    }
    val providerRateText = orderDetails.map {
        it?.provider?.let { provider ->
            "(${provider.averageRate.orZero().roundHalfUpToIntOrFloat(1)})"
        }.orEmpty()
    }

    val addressTitle = orderDetails.map { it?.address?.title.orEmpty() }
    val addressDetails = orderDetails.map { it?.address?.address.orEmpty() }

    val date = orderDetails.map { it?.orderedAtDate.orEmpty() }
    val time = orderDetails.map { it?.orderedAtTime.orEmpty() }

    val description = orderDetails.map { it?.extraNotes.orEmpty() }

    val trackingEnabled = orderStatus.map { it == ApiOrderStatus.ON_THE_WAY }

    val servicesTitleDrawableRes = orderStatus.map {
        when (it) {
            ApiOrderStatus.PENDING, ApiOrderStatus.CANCELLED, ApiOrderStatus.ACCEPTED, ApiOrderStatus.REJECTED -> R.drawable.ic_services_agreed_up_on
            ApiOrderStatus.ON_THE_WAY -> R.drawable.ic_services_agreed_up_on_black
            ApiOrderStatus.ARRIVED -> R.drawable.ic_services_agreed_up_on_black
            ApiOrderStatus.WORK_STARTED -> R.drawable.ic_services_agreed_up_on_black
            ApiOrderStatus.FINISHED -> R.drawable.ic_services_agreed_up_on_black
        }
    }

    val locationTitleDrawableRes = orderStatus.map {
        when (it) {
            ApiOrderStatus.PENDING, ApiOrderStatus.CANCELLED, ApiOrderStatus.ACCEPTED, ApiOrderStatus.REJECTED -> R.drawable.ic_location_yellow
            ApiOrderStatus.ON_THE_WAY -> R.drawable.ic_location_yellow_black
            ApiOrderStatus.ARRIVED -> R.drawable.ic_location_yellow_black
            ApiOrderStatus.WORK_STARTED -> R.drawable.ic_location_yellow_black
            ApiOrderStatus.FINISHED -> R.drawable.ic_location_yellow_black
        }
    }

    val canCallOrChatProvider = orderStatus.map {
        it != ApiOrderStatus.FINISHED && it != ApiOrderStatus.CANCELLED && it != ApiOrderStatus.PENDING
    }

    fun goToProviderDetails(view: View) {
        val provider = orderDetails.value?.provider ?: return

        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.provider.details",
            paths = arrayOf(provider.toJson(gson))
        )
    }
    fun chatWithProvider(view: View) {
        val providerId = orderDetails.value?.provider?.id ?: return
        val orderId = orderDetails.value?.id ?: return
        val imageUrl = orderDetails.value?.provider?.imageUrl
        val otherName = orderDetails.value?.provider?.name ?: return

        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.chat.details",
            paths = arrayOf(providerId.toString(), orderId.toString())
        )
    }
    fun callProvider(view: View) {
        view.context.launchDialNumber(orderDetails.value?.provider?.phone.orEmpty())
    }

    fun trackProvider(view: View) {
        val orderDetails = orderDetails.value ?: return
        val provider = orderDetails.provider ?: return

        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.location.tracking",
            paths = arrayOf(
                orderDetails.id.toString(),
                orderDetails.address?.latitude.orZero().toString(),
                orderDetails.address?.longitude.orZero().toString(),
                provider.name,
                provider.imageUrl,
                provider.averageRate.toString(),
            )
        )
    }

    fun cancelOrder(view: View) {
        val context = view.context ?: return

        val cancellationFeesPercent = orderDetails.value?.cancellationFeesPercent
            ?.roundHalfUpToIntOrFloat(1)?.toFloat() ?: return

        val text = if (cancellationFeesPercent > 0f) {
            context.getString(R.string.are_you_sure_about_order_cancellation_with_fees_part_1) +
                "$cancellationFeesPercent" +
                context.getString(R.string.are_you_sure_about_order_cancellation_with_fees_part_2)
        }else {
            context.getString(R.string.are_you_sure_about_order_cancellation)
        }

        /*view.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
            "dialog-dest",
            "com.grand.hassan.shared.cancel.order.dialog",
            orderId.toString(),
            text
        )*/

        val details = orderDetails.value ?: return

        if (orderStatus.value == ApiOrderStatus.ACCEPTED
            || orderStatus.value == ApiOrderStatus.PENDING) {
            view.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                "dialog-dest",
                "com.grand.hassan.shared.cancel.order.dialog",
                details.id.toString(),
                text
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

    fun rateProvider(view: View) {
        view.findNavControllerOfProject().navigate(
            OrderDetailsFragmentDirections.actionDestOrderDetailsToDestRateProvider2Dialog(
                orderDetails.value?.provider?.id ?: return
            )
        )
    }

}
