package com.maproductions.mohamedalaa.hassanp.presentation.service

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentAddingServicesBinding
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentOrderDetailsBinding
import com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel.OrderDetailsViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.service.viewModel.AddingServicesViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.core.extensions.withCustomAdapters
import com.maproductions.mohamedalaa.shared.core.extensions.withDefaultHeaderAndFooterAdapters
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.base.adapters.LSAdapterLoadingErrorEmpty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AddingServicesFragment : MABaseFragment<FragmentAddingServicesBinding>() {

    private val viewModel by viewModels<AddingServicesViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_adding_services

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.servicesTypesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.servicesTypesRecyclerView?.adapter = viewModel.adapter
            .withDefaultHeaderAndFooterAdapters()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.services.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) { response ->
            val list = response.data.orEmpty()

            val current = viewModel.currentServices.mapNotNull { item ->
                list.firstOrNull { it.id == item.id }?.let {
                    it.id to ServiceInCategoryWithCount(it, item.count)
                }
            }.associate {
                it
            }

            viewModel.adapter.addExistingSelections(current)

            binding?.servicesTypesRecyclerView?.post {
                viewModel.adapter.notifyDataSetChanged()
            } ?: viewModel.adapter.notifyDataSetChanged()
        }
    }

}
