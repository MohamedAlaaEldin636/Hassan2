package com.maproductions.mohamedalaa.hassanp.presentation.home

import android.view.Gravity
import android.view.Window
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.DialogFragmentNewOrderBinding
import com.maproductions.mohamedalaa.hassanp.databinding.DialogFragmentStopRecievingOrdersBinding
import com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel.NewOrderViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel.StopRecievingOrdersViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StopRecievingOrdersDialogFragment : MADialogFragment<DialogFragmentStopRecievingOrdersBinding>() {

    companion object {
        const val SAVED_STATE_HOURS = "StopRecievingOrdersDialogFragment.SAVED_STATE_HOURS"
    }

    private val viewModel by viewModels<StopRecievingOrdersViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_stop_recieving_orders

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(com.maproductions.mohamedalaa.shared.R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = com.maproductions.mohamedalaa.shared.R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
