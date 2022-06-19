package com.maproductions.mohamedalaa.hassanu.presentation.discount

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentCodesOfDiscountsBinding
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentMyAddressesBinding
import com.maproductions.mohamedalaa.hassanu.presentation.address.viewModel.MyAddressesViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.discount.viewModel.CodesOfDiscountsViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.withDefaultHeaderAndFooterAdapters
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CodesOfDiscountsFragment : MABaseFragment<FragmentCodesOfDiscountsBinding>() {

    private val viewModel by viewModels<CodesOfDiscountsViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_codes_of_discounts

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.layoutManager = GridLayoutManager(
            requireContext(), 2
        )
        binding?.recyclerView?.adapter = viewModel.adapter
            .withDefaultHeaderAndFooterAdapters()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.codes.collectLatest { pagingData ->
                    viewModel.adapter.submitData(pagingData.map { it.code.orEmpty() })
                }
            }
        }
    }

}