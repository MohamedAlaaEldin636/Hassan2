package com.maproductions.mohamedalaa.shared.presentation.onBoard

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.databinding.FragmentOnBoardBinding
import com.maproductions.mohamedalaa.shared.databinding.FragmentSplashBinding
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.onBoard.viewModel.OnBoardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardFragment : MABaseFragment<FragmentOnBoardBinding>() {

    private val viewModel by viewModels<OnBoardViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_on_board

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlowList) { response ->
            viewModel.list.value = response.data.orEmpty()

            viewModel.adapterSliders.updateList(response.data.orEmpty())
        }

        binding?.sliderView?.setSliderAdapter(viewModel.adapterSliders)
        binding?.sliderView?.post {
            binding?.sliderView?.isAutoCycle = false

            binding?.sliderView?.setIndicatorEnabled(true)
            binding?.sliderView?.indicatorSelectedColor = requireContext().getColor(R.color.colorPrimaryDark)
            binding?.sliderView?.indicatorUnselectedColor = requireContext().getColor(R.color.colorPrimaryLight)
        }
        binding?.sliderView?.setCurrentPageListener {
            viewModel.currentPageIndex.value = it
        }
    }

}
