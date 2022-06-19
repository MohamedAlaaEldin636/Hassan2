package com.maproductions.mohamedalaa.hassanu.presentation.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentOrderAdditionsBinding
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentOrderDetailsBinding
import com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel.OrderAdditionsViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel.OrderDetailsViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrderDetails
import com.maproductions.mohamedalaa.shared.domain.orders.ServiceInOrderDetails
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderAdditionsFragment : MABaseFragment<FragmentOrderAdditionsBinding>() {

    private val viewModel by viewModels<OrderAdditionsViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_order_additions

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel?.titleToolbar?.postValue(getString(SR.string.order_additions_9))

        binding?.servicesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.servicesRecyclerView?.adapter = viewModel.adapterServicesDetails

        binding?.costRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.costRecyclerView?.adapter = viewModel.adapterServicesSummary

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleOrderDetails) { response ->
            val orderDetails = response.data ?: return@handleRetryAbleFlowWithMustHaveResultWithNullability

            activityViewModel?.titleToolbar?.postValue(
                "${getString(SR.string.order_additions_9)} ${orderDetails.orderNumber.orEmpty()}"
            )

            viewModel.adapterServicesDetails.submitList(orderDetails.additionalServices.orEmpty().map {
                ServiceInCategoryWithCount(
                    ServiceInCategory(
                        it.id.orZero(),
                        it.categoryId.orZero(),
                        -1,
                        it.price.orZero(),
                        it.name.orEmpty()
                    ),
                    it.count.orZero()
                )
            })
            viewModel.adapterServicesSummary.submitList(listOf(
                orderDetails.sumOfAdditionalServices.orZero().createServiceInCategoryWithCount(getString(SR.string.added_cost)),
                orderDetails.sumOfOriginalServices.orZero().createServiceInCategoryWithCount(getString(SR.string.previously_agreed_on_services_cost)),
                orderDetails.sumOfAllServices.orZero().createServiceInCategoryWithCount(getString(SR.string.total_order_cost))
            ))
            viewModel.amountToBePaid.value = "${orderDetails.sumOfAllServices.orZero()} ${getString(SR.string.egp)}"
        }
    }

    private fun Float.createServiceInCategoryWithCount(name: String): ServiceInCategoryWithCount {
        return ServiceInCategoryWithCount(
            ServiceInCategory(0, 0, 0, this, name),
            1
        )
    }

}
