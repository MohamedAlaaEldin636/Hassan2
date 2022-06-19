package com.maproductions.mohamedalaa.hassanu.presentation.service

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentServicesLocationSelectionBinding
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentServicesSelectionBinding
import com.maproductions.mohamedalaa.hassanu.presentation.address.AddNewAddressFragment
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.ServicesLocationSelectionViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.ServicesSelectionViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.actOnGetIfNotInitialValueOrGetLiveData
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesLocationSelectionFragment : MABaseFragment<FragmentServicesLocationSelectionBinding>() {

    private val viewModel by viewModels<ServicesLocationSelectionViewModel>()

    private val args by navArgs<ServicesLocationSelectionFragmentArgs>()

    override fun getLayoutId(): Int = R.layout.fragment_services_location_selection

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel?.titleToolbar?.postValue(args.categoryName)

        binding?.recyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.recyclerView?.adapter = viewModel.adapter

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) {
            viewModel.adapter.submitList(it.data.orEmpty())
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            AddNewAddressFragment.KEY_FRAGMENT_ADDED_ADDRESS_SUCCESSFULLY,
            false,
            viewLifecycleOwner,
            { it == true }
        ) {
            viewModel.retryAbleFlow.retry()

            handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) {
                viewModel.adapter.submitList(it.data.orEmpty(), true)
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.shared.add.new.address",
                paths = arrayOf(it)
            )
        }
    }

}
