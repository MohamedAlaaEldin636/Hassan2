package com.maproductions.mohamedalaa.shared.presentation.order

import android.view.Gravity
import android.view.Window
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.databinding.DialogFragmentCancelOrderBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import com.maproductions.mohamedalaa.shared.presentation.order.viewModel.CancelOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CancelOrderDialogFragment : MADialogFragment<DialogFragmentCancelOrderBinding>() {

    companion object {
        const val SAVED_STATE_DONE_CANCEL_ORDER = "CancelOrderDialogFragment.SAVED_STATE_DONE_CANCEL_ORDER"
    }

    private val viewModel by viewModels<CancelOrderViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_cancel_order

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
