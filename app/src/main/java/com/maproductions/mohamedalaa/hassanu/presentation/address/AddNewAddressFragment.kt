package com.maproductions.mohamedalaa.hassanu.presentation.address

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentAddingNewAddressBinding
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentServicesSelectionBinding
import com.maproductions.mohamedalaa.hassanu.presentation.address.viewModel.AddNewAddressViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.ServicesSelectionViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewAddressFragment : MABaseFragment<FragmentAddingNewAddressBinding>() {

    companion object {
        const val KEY_FRAGMENT_ADDED_ADDRESS_SUCCESSFULLY = "AddNewAddressFragment.KEY_FRAGMENT_ADDED_ADDRESS_SUCCESSFULLY"
    }

    private val viewModel by viewModels<AddNewAddressViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_adding_new_address

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlowCities) {
            viewModel.listOfCities.value = it.data

            viewModel.loadAreas(this)
        }
    }

}
