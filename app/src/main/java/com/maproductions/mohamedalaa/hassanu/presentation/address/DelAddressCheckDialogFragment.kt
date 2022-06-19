package com.maproductions.mohamedalaa.hassanu.presentation.address

import android.view.Gravity
import android.view.Window
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentAcceptedProviderServiceRequestBinding
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentDelAddressCheckBinding
import com.maproductions.mohamedalaa.hassanu.presentation.address.viewModel.DelAddressCheckViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.AcceptedProviderServiceRequestViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DelAddressCheckDialogFragment : MADialogFragment<DialogFragmentDelAddressCheckBinding>() {

    companion object {
        const val KEY_FRAGMENT_DELETION_ID = "DelAddressCheckDialogFragment.KEY_FRAGMENT_DELETION_ID"
    }

    private val viewModel by viewModels<DelAddressCheckViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_del_address_check

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(com.maproductions.mohamedalaa.shared.R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = com.maproductions.mohamedalaa.shared.R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
