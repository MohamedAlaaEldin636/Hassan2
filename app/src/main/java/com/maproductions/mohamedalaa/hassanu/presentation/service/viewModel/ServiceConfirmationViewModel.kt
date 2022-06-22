package com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel

import android.app.Application
import android.net.Uri
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServiceConfirmationFragment
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServiceConfirmationFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServiceConfirmationFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.RVItemServiceNameAndPrice
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.home.repository.RepoHome
import com.maproductions.mohamedalaa.shared.domain.home.DeliveryData
import com.maproductions.mohamedalaa.shared.domain.home.ResponseCheckPromoCode
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.presentation.base.adapters.RVItemImageRect
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class ServiceConfirmationViewModel @Inject constructor(
    application: Application,
    val args: ServiceConfirmationFragmentArgs,
    private val repoHome: RepoHome,
    private val gson: Gson,
) : AndroidViewModel(application) {

    private val currentDateTime = args.orderedAt.convertToLocalDateTimeFromFormat1()

    val time = currentDateTime.formatTime1(myApp)
    val date = currentDateTime.formatDate1(myApp)

    val description = args.extraNotes

    val showValidDiscount = MutableLiveData(false)

    val showImages = MutableLiveData(true)

    val adapterImages = RVItemImageRect(false)

    val services = args.jsonOfServices.fromJson<List<ServiceInCategoryWithCount>>(gson)

    val adapterServicesDetails = RVItemServiceNameAndPrice(services, false)
    val adapterServicesSummary = RVItemServiceNameAndPrice(getServicesSummaryList(null), true)

    val discount = MutableLiveData<ResponseCheckPromoCode>()

    fun performDiscountCodeCheck() = TextView.OnEditorActionListener { textView, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            val code = textView.text?.toString().orEmpty()

            if (code.isNotEmpty()) {
                textView.findFragment<ServiceConfirmationFragment>().executeOnGlobalLoadingAndAutoHandleRetryCancellable(
                    afterShowingLoading = {
                        repoHome.checkPromoCode(code)
                    },
                    afterHidingLoading = { response ->
                        discount.value = response

                        showValidDiscount.value = response != null
                    }
                )
            }

            return@OnEditorActionListener true
        }

        false
    }

    fun confirmOrder(view: View) {
        val fragment = view.findFragment<ServiceConfirmationFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                val imagesUrisAsStrings = args.jsonImagesUrisAsStrings.let { json ->
                    if (json.isEmpty()) emptyList() else json.fromJson<List<String>>(gson)
                }.mapNotNull {
                    kotlin.runCatching { Uri.parse(it) }.getOrNull()
                }

                val total = adapterServicesSummary.currentList.last().serviceInCategory.price.minus(
                    adapterServicesSummary.currentList[1].serviceInCategory.price
                )

                Timber.e("promo id ${discount.value?.id}")

                repoHome.createOrder(
                    args.categoryId,

                    args.jsonOfServices.fromJson(gson),

                    args.addressId,

                    args.orderedAt,
                    args.orderType,

                    imagesUrisAsStrings.mapNotNull {
                        it.createMultipartBodyPart(myApp, ApiConst.Query.IMAGES + "[]")
                    },

                    args.extraNotes,

                    total,

                    discount.value?.id
                )
            },
            afterHidingLoading = {
                fragment.findNavControllerOfProject().navigate(
                    ServiceConfirmationFragmentDirections
                        .actionDestServiceConfirmationToDestPendingProviderServiceRequestDialog(it.orZero())
                )
            }
        )
    }

    fun getServicesSummaryList(response: ResponseCheckPromoCode?): List<ServiceInCategoryWithCount> {
        val totalServicesCost = services.sumOf {
            it.serviceInCategory.price.toBigDecimal().multiply(
                it.count.toBigDecimal()
            )
        }.toFloat()

        val deliveryCost = args.jsonDeliveryData.fromJson<DeliveryData>(gson).deliveryFees

        var isPercent = false
        val discount = if (response != null) {
            if (response.isPercent) {
                // Negative indicate percent isa.
                isPercent = true
                response.value * -1
            }else {
                response.value
            }
        }else {
            0f
        }

        val discountAsFixed = if (!isPercent) discount else discount.absoluteValue.let {
            totalServicesCost * it / 100f
        }

        val total = (totalServicesCost - discountAsFixed + deliveryCost).coerceAtLeast(0f)

        return listOf(
            totalServicesCost.createServiceInCategoryWithCount(myApp.getString(SR.string.services_cost)),
            deliveryCost.createServiceInCategoryWithCount(myApp.getString(SR.string.delivery_cost)),
            discount.createServiceInCategoryWithCount(myApp.getString(SR.string.discount_value)),
            total.createServiceInCategoryWithCount(myApp.getString(SR.string.total_cost))
        )
    }

    private fun Float.createServiceInCategoryWithCount(name: String): ServiceInCategoryWithCount {
        return ServiceInCategoryWithCount(
            ServiceInCategory(0, 0, 0, this, name),
            1
        )
    }

}
