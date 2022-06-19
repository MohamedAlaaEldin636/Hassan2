package com.maproductions.mohamedalaa.hassanu.presentation.order

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentRateProvider2Binding
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentRateProviderBinding
import com.maproductions.mohamedalaa.hassanu.presentation.global.viewModel.RateProviderViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel.RateProvider2ViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateProvider2DialogFragment : MADialogFragment<DialogFragmentRateProvider2Binding>() {

    private val viewModel by viewModels<RateProvider2ViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_rate_provider_2

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(com.maproductions.mohamedalaa.shared.R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = com.maproductions.mohamedalaa.shared.R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
