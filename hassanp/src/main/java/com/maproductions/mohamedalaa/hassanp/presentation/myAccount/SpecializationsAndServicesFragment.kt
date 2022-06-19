package com.maproductions.mohamedalaa.hassanp.presentation.myAccount

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentSpecializationsAndServicesBinding
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.PreviousWorksViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.SpecializationsAndServicesViewModel
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SpecializationsAndServicesFragment : MABaseFragment<FragmentSpecializationsAndServicesBinding>() {

    private val viewModel by viewModels<SpecializationsAndServicesViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_specializations_and_services

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.recyclerView?.adapter = viewModel.adapter

        viewModel.services.observe(viewLifecycleOwner) {
            viewModel.adapter.submitList(it.orEmpty().map { item -> item.name.orEmpty() })
        }
    }

}

