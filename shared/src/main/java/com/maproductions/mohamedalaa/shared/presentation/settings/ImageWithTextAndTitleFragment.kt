package com.maproductions.mohamedalaa.shared.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.databinding.FragmentImageWithTextAndTitleBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.settings.viewModel.ImageWithTextAndTitleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageWithTextAndTitleFragment : MABaseFragment<FragmentImageWithTextAndTitleBinding>() {

    private val viewModel by viewModels<ImageWithTextAndTitleViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_image_with_text_and_title

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) {
            viewModel.imageUrl.value = it.data?.imageUrl.orEmpty()
            viewModel.textAsHtml.value = it.data?.textAsHtml.orEmpty()
        }
    }

}
