package com.maproductions.mohamedalaa.hassanu.presentation.order

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentOrderDetailsBinding
import com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel.OrderDetailsViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrderDetails
import com.maproductions.mohamedalaa.shared.domain.orders.ServiceInOrderDetails
import com.maproductions.mohamedalaa.shared.domain.pusher.ResponsePusherOrder
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.LocationTrackingFragment
import com.maproductions.mohamedalaa.shared.presentation.order.CancelOrderDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailsFragment : MABaseFragment<FragmentOrderDetailsBinding>() {

    private val viewModel by viewModels<OrderDetailsViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_order_details

    @Inject
    lateinit var gson: Gson

    private val channelEvent by lazy {
        PusherUtils.getChannelEvent(
            PusherUtils.getChannelNameOnChangeOrderStatusForOrder(viewModel.args.id),
            PusherUtils.EVENT_NAME_ON_CHANGE_ORDER_STATUS_FOR_ORDER
        ) { data ->
            Timber.e("order status data ->\n$data")

            //val response = ResponsePusherOrder.fromJson(data, gson)//data.fromJson<>(gson)

            Handler(Looper.getMainLooper()).post {
                viewModel.retryAbleOrderDetails.retry()

                kotlin.runCatching {
                    findNavControllerOfProject().executeWhenLoadingEndsIfExists {
                        loadOrderDetails()
                    }
                }
            }
        }
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelEvent.subscribe()

        binding?.imagesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        binding?.imagesRecyclerView?.adapter = viewModel.adapterImages

        binding?.servicesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.servicesRecyclerView?.adapter = viewModel.adapterServicesDetails

        binding?.costRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.costRecyclerView?.adapter = viewModel.adapterServicesSummary

        loadOrderDetails()

        findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            CancelOrderDialogFragment.SAVED_STATE_DONE_CANCEL_ORDER,
            false,
            viewLifecycleOwner,
            { it == true }
        ) {
            findNavControllerOfProject().navigateUp()
        }

        findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            LocationTrackingFragment.SAVED_STATE_PERFORM_REFRESH,
            false,
            viewLifecycleOwner,
            { it == true }
        ) {
            viewModel.retryAbleOrderDetails.retry()

            loadOrderDetails()
        }
    }

    override fun onDestroyView() {
        channelEvent.unsubscribe()

        super.onDestroyView()
    }

    private fun loadOrderDetails() {
        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleOrderDetails) { response ->
            viewModel.orderDetails.value = response.data

            val orderDetails = response.data ?: return@handleRetryAbleFlowWithMustHaveResultWithNullability

            viewModel.adapterImages.submitList(orderDetails.imagesUrls.map { MAImage.ILink(it.fileUrl) })
            viewModel.showImages.value = orderDetails.imagesUrls.isNotEmpty()

            viewModel.adapterServicesDetails.submitList(orderDetails.services.map {
                ServiceInCategoryWithCount(
                    ServiceInCategory(
                        it.serviceId,
                        -1,
                        -1,
                        it.price,
                        it.name.orEmpty()
                    ),
                    it.count
                )
            })
            viewModel.adapterServicesSummary.submitList(
                getServicesSummaryList(orderDetails, orderDetails.services)
            )
        }
    }

    private fun getServicesSummaryList(response: ResponseOrderDetails, services: List<ServiceInOrderDetails>): List<ServiceInCategoryWithCount> {
        val deliveryCost = response.deliveryCost

        val total = (response.total + deliveryCost).coerceAtLeast(0f)

        val discount = response.promo?.let {
            if (it.isPercent) {
                // Negative indicate percent isa.
                it.value * -1
            }else {
                it.value
            }
        }

        return listOfNotNull(
            response.total.createServiceInCategoryWithCount(getString(com.maproductions.mohamedalaa.shared.R.string.services_cost)),
            deliveryCost.createServiceInCategoryWithCount(getString(com.maproductions.mohamedalaa.shared.R.string.delivery_cost)),
            discount?.createServiceInCategoryWithCount(getString(com.maproductions.mohamedalaa.shared.R.string.discount_value)),
            total.createServiceInCategoryWithCount(getString(com.maproductions.mohamedalaa.shared.R.string.total_cost))
        )
    }

    private fun Float.createServiceInCategoryWithCount(name: String): ServiceInCategoryWithCount {
        return ServiceInCategoryWithCount(
            ServiceInCategory(0, 0, 0, this, name),
            1
        )
    }

}
