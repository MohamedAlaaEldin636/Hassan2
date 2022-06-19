package com.maproductions.mohamedalaa.hassanp.presentation.order

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel.ConfirmFinishingWorkViewModel
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.hassanp.R as PR
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.databinding.DialogFragmentCancelOrderBinding
import com.maproductions.mohamedalaa.hassanp.databinding.DialogFragmentConfirmFinishingWorkBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import com.maproductions.mohamedalaa.shared.presentation.order.viewModel.CancelOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmFinishingWorkDialogFragment : MADialogFragment<DialogFragmentConfirmFinishingWorkBinding>() {

    private val viewModel by viewModels<ConfirmFinishingWorkViewModel>()

    override fun getLayoutId(): Int = PR.layout.dialog_fragment_confirm_finishing_work

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
