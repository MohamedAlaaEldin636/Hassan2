package com.maproductions.mohamedalaa.hassanu.presentation.settings

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.repeatOnLifecycle
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentGetDiscountsBinding
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentPersonalDataBinding
import com.maproductions.mohamedalaa.hassanu.presentation.settings.viewModel.GetDiscountsViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.settings.viewModel.PersonalDataViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.actOnGetIfNotInitialValueOrGetLiveData
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetDiscountsFragment : MABaseFragment<FragmentGetDiscountsBinding>() {

    private val viewModel by viewModels<GetDiscountsViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_get_discounts

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.textView4?.movementMethod = LinkMovementMethod.getInstance()

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) {
            viewModel.text3.value = getString(SR.string.get_discount_3_var, it.data?.appPoints.orEmpty())

            viewModel.yourLink.value = it.data?.link.orEmpty()

            viewModel.myPoints.value = it.data?.myPoints.orZero()
        }
    }

}
