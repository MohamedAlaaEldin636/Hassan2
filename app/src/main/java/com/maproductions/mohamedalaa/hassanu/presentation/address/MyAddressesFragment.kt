package com.maproductions.mohamedalaa.hassanu.presentation.address

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentMyAddressesBinding
import com.maproductions.mohamedalaa.hassanu.presentation.address.viewModel.MyAddressesViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyAddressesFragment : MABaseFragment<FragmentMyAddressesBinding>() {

    private val viewModel by viewModels<MyAddressesViewModel>()

    @Inject
    lateinit var gson: Gson

    override fun getLayoutId(): Int = R.layout.fragment_my_addresses

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.recyclerView?.adapter = viewModel.adapter

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) {
            Timber.e("abcdef MyAddressesFragment list 1 -> ${it.data}")

            viewModel.adapter.submitList(it.data.orEmpty())
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetLiveDataToRemoveKey(
            DelAddressCheckDialogFragment.KEY_FRAGMENT_DELETION_ID,
            -9,
            viewLifecycleOwner,
            { it != -9 }
        ) {
            Timber.e("abcdef MyAddressesFragment to be deleted id -> $it")

            viewModel.adapter.deleteAddress(it ?: return@actOnGetLiveDataToRemoveKey)

            findNavController().navigateDeepLinkWithOptions(
                "dialog-dest",
                "com.grand.hassan.shared.adding.address.dialog",
                paths = arrayOf(false.toString())
            )
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            findNavController().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.shared.add.new.address",
                paths = arrayOf(it)
            )
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            AddNewAddressFragment.KEY_FRAGMENT_ADDED_ADDRESS_SUCCESSFULLY,
            false,
            viewLifecycleOwner,
            { it == true }
        ) {
            findNavController().navigateDeepLinkWithOptions(
                "dialog-dest",
                "com.grand.hassan.shared.adding.address.dialog",
                paths = arrayOf(true.toString())
            )

            viewModel.retryAbleFlow.retry()

            handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) {
                Timber.e("abcdef MyAddressesFragment list 2 -> ${it.data}")

                viewModel.adapter.submitList(it.data.orEmpty())
            }
        }
    }

}
