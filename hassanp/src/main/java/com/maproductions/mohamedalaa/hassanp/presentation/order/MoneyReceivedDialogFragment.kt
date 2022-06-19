package com.maproductions.mohamedalaa.hassanp.presentation.order

import android.view.Gravity
import android.view.Window
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.DialogFragmentAcceptedOrderBinding
import com.maproductions.mohamedalaa.hassanp.databinding.DialogFragmentMoneyReceivedBinding
import com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel.AcceptedOrderViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel.MoneyReceivedViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoneyReceivedDialogFragment : MADialogFragment<DialogFragmentMoneyReceivedBinding>() {

    private val viewModel by viewModels<MoneyReceivedViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_money_received

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(com.maproductions.mohamedalaa.shared.R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = com.maproductions.mohamedalaa.shared.R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
