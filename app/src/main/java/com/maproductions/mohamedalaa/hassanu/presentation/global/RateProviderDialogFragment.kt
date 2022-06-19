package com.maproductions.mohamedalaa.hassanu.presentation.global

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentRateProviderBinding
import com.maproductions.mohamedalaa.hassanu.presentation.global.viewModel.RateProviderViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint
import com.maproductions.mohamedalaa.shared.R as SR

@AndroidEntryPoint
class RateProviderDialogFragment : MADialogFragment<DialogFragmentRateProviderBinding>() {

    private val viewModel by viewModels<RateProviderViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_rate_provider

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(SR.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = SR.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
