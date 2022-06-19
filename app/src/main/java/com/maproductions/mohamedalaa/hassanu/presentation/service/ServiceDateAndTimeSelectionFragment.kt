package com.maproductions.mohamedalaa.hassanu.presentation.service

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentServiceDateAndTimeSelectionBinding
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentServicesLocationSelectionBinding
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.ServiceDateAndTimeSelectionViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.ServicesLocationSelectionViewModel
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceDateAndTimeSelectionFragment : MABaseFragment<FragmentServiceDateAndTimeSelectionBinding>() {

    private val viewModel by viewModels<ServiceDateAndTimeSelectionViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_service_date_and_time_selection

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel?.titleToolbar?.postValue(viewModel.args.categoryName)
    }

}
