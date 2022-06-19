package com.maproductions.mohamedalaa.hassanu.presentation.service

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentServicesSelectionBinding
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.ServicesSelectionViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.withCustomAdapters
import com.maproductions.mohamedalaa.shared.core.extensions.withDefaultHeaderAndFooterAdapters
import com.maproductions.mohamedalaa.shared.databinding.FragmentSearchQueriesBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.base.adapters.LSAdapterLoadingErrorEmpty
import com.maproductions.mohamedalaa.shared.presentation.search.viewModel.SearchQueriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ServicesSelectionFragment : MABaseFragment<FragmentServicesSelectionBinding>() {

    private val viewModel by viewModels<ServicesSelectionViewModel>()

    private val args by navArgs<ServicesSelectionFragmentArgs>()

    override fun getLayoutId(): Int = R.layout.fragment_services_selection

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel?.titleToolbar?.postValue(args.categoryName)

        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val headerAdapterCurrent = LSAdapterLoadingErrorEmpty(viewModel.adapter, false)
        val footerAdapterFinished = LSAdapterLoadingErrorEmpty(viewModel.adapter, true)
        binding?.recyclerView?.adapter = viewModel.adapter.withCustomAdapters(
            headerAdapterCurrent,
            footerAdapterFinished
        )
        //binding?.recyclerView?.adapter = viewModel.adapter.withDefaultHeaderAndFooterAdapters()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.services.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }
    }

}
