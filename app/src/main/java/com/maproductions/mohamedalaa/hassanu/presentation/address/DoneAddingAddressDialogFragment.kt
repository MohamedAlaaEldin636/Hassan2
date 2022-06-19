package com.maproductions.mohamedalaa.hassanu.presentation.address

import android.view.Gravity
import android.view.Window
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentDelAddressCheckBinding
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentDoneAddingAddressBinding
import com.maproductions.mohamedalaa.hassanu.presentation.address.viewModel.DelAddressCheckViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.address.viewModel.DoneAddingAddressViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoneAddingAddressDialogFragment : MADialogFragment<DialogFragmentDoneAddingAddressBinding>() {

    private val viewModel by viewModels<DoneAddingAddressViewModel>()

    override val canceledOnTouchOutside: Boolean
        get() = false

    override fun getLayoutId(): Int = R.layout.dialog_fragment_done_adding_address

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(com.maproductions.mohamedalaa.shared.R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = com.maproductions.mohamedalaa.shared.R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
