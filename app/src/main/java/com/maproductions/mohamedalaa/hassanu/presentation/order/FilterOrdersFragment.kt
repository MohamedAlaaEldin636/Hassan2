package com.maproductions.mohamedalaa.hassanu.presentation.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentFilterOrdersBinding
import com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel.FilterOrdersViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.core.extensions.withCustomAdapters
import com.maproductions.mohamedalaa.shared.core.extensions.withDefaultHeaderAndFooterAdapters
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterOrdersFragment : MABaseFragment<FragmentFilterOrdersBinding>() {

    companion object {
        const val SAVED_STATE_CATEGORY_ID_AND_CITY_ID = "FilterOrdersFragment.SAVED_STATE_CATEGORY_ID_AND_CITY_ID"
    }

    private val viewModel by viewModels<FilterOrdersViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_filter_orders

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.servicesTypesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.servicesTypesRecyclerView?.adapter = viewModel.adapterCategories
            .withDefaultHeaderAndFooterAdapters()

        binding?.servicesCitiesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.servicesCitiesRecyclerView?.adapter = viewModel.adapterCities

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collectLatest {
                    viewModel.adapterCategories.submitData(it)
                }
            }
        }

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleCities) {
            viewModel.adapterCities.submitList(it.data.orEmpty())
        }
    }

}
