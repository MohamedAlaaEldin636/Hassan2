package com.maproductions.mohamedalaa.hassanp.presentation.myAccount

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentEditSpecializationsAndServicesBinding
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.EditSpecializationsAndServicesViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.withDefaultHeaderAndFooterAdapters
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditSpecializationsAndServicesFragment : MABaseFragment<FragmentEditSpecializationsAndServicesBinding>() {

    private val viewModel by viewModels<EditSpecializationsAndServicesViewModel>()

    private val loadStateListener: (CombinedLoadStates) -> Unit = {
        if (viewModel.selectedCategoryId.value == null) {
            viewModel.selectedCategoryId.value = viewModel.adapterCategories.snapshot().items
                .firstOrNull()?.id
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_edit_specializations_and_services

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.pickProfessionRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.pickProfessionRecyclerView?.adapter = viewModel.adapterCategories
            .withDefaultHeaderAndFooterAdapters()

        binding?.servicesCitiesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.servicesCitiesRecyclerView?.adapter = viewModel.adapterServices
            .withDefaultHeaderAndFooterAdapters()

        viewModel.adapterCategories.addLoadStateListener(loadStateListener)

        viewModel.selectedCategoryId.distinctUntilChanged().observe(viewLifecycleOwner) { newValue ->
            if (newValue != null) {
                viewModel.adapterCategories.removeLoadStateListener(loadStateListener)

                viewModel.adapterServices.clearSelection()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.categories.collectLatest {
                        viewModel.adapterCategories.submitData(it)
                    }
                }
                launch {
                    viewModel.services.collectLatest {
                        viewModel.adapterServices.submitData(it)
                    }
                }
            }
        }
    }

}
