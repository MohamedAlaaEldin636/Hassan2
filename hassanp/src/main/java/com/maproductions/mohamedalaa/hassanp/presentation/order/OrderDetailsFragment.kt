package com.maproductions.mohamedalaa.hassanp.presentation.order

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.SphericalUtil
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.core.changeOrderNotOnTheWay
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentOrderDetailsBinding
import com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel.OrderDetailsViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationHandler
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrderDetails
import com.maproductions.mohamedalaa.shared.domain.orders.ServiceInOrderDetails
import com.maproductions.mohamedalaa.shared.domain.pusher.ResponsePusherOrder
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.order.CancelOrderDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailsFragment : MABaseFragment<FragmentOrderDetailsBinding>(), LocationHandler.Listener {

    companion object {
        const val SAVED_STATE_ORDER_DETAILS_MADE_CHANGE = "OrderDetailsFragment.SAVED_STATE_ORDER_DETAILS_MADE_CHANGE"
    }

    @Inject
    lateinit var gson: Gson

    private val channelEvent by lazy {
        PusherUtils.getChannelEvent(
            PusherUtils.getChannelNameOnChangeOrderStatusForOrder(viewModel.args.orderId),
            PusherUtils.EVENT_NAME_ON_CHANGE_ORDER_STATUS_FOR_ORDER
        ) { data ->
            Timber.e("order status data ->\n$data")

            val response = ResponsePusherOrder.fromJson(data, gson)//data.fromJson<>(gson)

            Handler(Looper.getMainLooper()).post {
                kotlin.runCatching {
                    findNavControllerOfProject().executeWhenLoadingEndsIfExists {
                        viewModel.viewModelScope.launch {
                            var job: Job? = null
                            job = launch {
                                viewModel.makePusherSuspendWork.collectLatest {
                                    if (!it) {
                                        Timber.e("response.order.status.statusOfOrder " +
                                                "${response.order?.statusOfOrder} " +
                                                "!= viewModel.orderStatus.value " +
                                                "${viewModel.orderDetails.value?.statusOfOrder}")

                                        if (response.order?.statusOfOrder != viewModel.orderDetails.value?.statusOfOrder) {
                                            viewModel.retryAbleOrderDetails.retry()

                                            getOrderDetails()
                                        }

                                        job?.cancel()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private val viewModel by viewModels<OrderDetailsViewModel>()

    lateinit var locationHandler: LocationHandler
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        locationHandler = LocationHandler(
            this,
            lifecycle,
            requireContext(),
            this
        )

        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int = R.layout.fragment_order_details

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

        getOrderDetails()

        findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            CancelOrderDialogFragment.SAVED_STATE_DONE_CANCEL_ORDER,
            false,
            viewLifecycleOwner,
            { it == true }
        ) {
            viewModel.orderDetails.value?.id?.also { id ->
                changeOrderNotOnTheWay(id)
            }

            val navController = findNavControllerOfProject()

            navController.navigateUp()

            navController.currentBackStackEntry?.savedStateHandle?.set(
                SAVED_STATE_ORDER_DETAILS_MADE_CHANGE,
                true
            )
        }
    }

    fun getOrderDetails() {
        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleOrderDetails) { response ->
            viewModel.orderDetails.value = response.data

            viewModel.makePusherSuspendWork.value = false

            val orderDetails = response.data ?: return@handleRetryAbleFlowWithMustHaveResultWithNullability

            if (orderDetails.statusOfOrder == ApiOrderStatus.ON_THE_WAY) {
                locationHandler.requestLocationUpdates()
            }else {
                locationHandler.stopLocationUpdates()
            }

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
                getServicesSummaryList(orderDetails)
            )
        }
    }

    private fun getServicesSummaryList(response: ResponseOrderDetails): List<ServiceInCategoryWithCount> {
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

    override fun onChangeLocationSuccess(location: Location) {
        viewModel.location = location

        val (latitude, longitude) = viewModel.orderDetails.value?.address?.let {
            it.latitude to it.longitude
        } ?: return

        val distanceInKm = SphericalUtil.computeDistanceBetween(
            LatLng(latitude, longitude),
            LatLng(location.latitude, location.longitude),
        ).let {
            it / 1000.0
        }

        viewModel.canPressOnArrived.value = distanceInKm <= 5
    }

    override fun onChangeLocationFailure(context: Context?, exception: Exception?) {
        executeShowingErrorOnce(
            false,
            getString(com.maproductions.mohamedalaa.shared.R.string.there_is_an_error_in_detecting_your_location)
        ) {
            locationHandler.requestLocationUpdates()
        }
    }

    override fun onDestroyView() {
        locationHandler.stopLocationUpdates()

        channelEvent.unsubscribe()

        super.onDestroyView()
    }

}