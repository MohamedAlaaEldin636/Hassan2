package com.maproductions.mohamedalaa.hassanp.presentation.myAccount

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentMyReviewsBinding
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentWalletBinding
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.MyReviewsViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.WalletViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.core.extensions.roundHalfUpToIntOrFloat
import com.maproductions.mohamedalaa.shared.core.extensions.withDefaultHeaderAndFooterAdapters
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class MyReviewsFragment : MABaseFragment<FragmentMyReviewsBinding>() {

    private val viewModel by viewModels<MyReviewsViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_my_reviews

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.recyclerView?.adapter = viewModel.adapter.withDefaultHeaderAndFooterAdapters()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reviews.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) { response ->
            val rate = response.data?.averageRate?.roundHalfUpToIntOrFloat(1)?.toFloat()
                ?: return@handleRetryAbleFlowWithMustHaveResultWithNullability

            viewModel.ratingAsPercent.value = (rate * 20f).roundToInt()

            viewModel.ratingBetweenBrackets.value = "($rate)"
        }
    }

}
