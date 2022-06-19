package com.maproductions.mohamedalaa.hassanu.presentation.global

import android.view.Gravity
import android.view.Window
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentGetDiscountOodeBinding
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentMoveToProviderAppBinding
import com.maproductions.mohamedalaa.hassanu.presentation.global.viewModel.MoveToProviderAppViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.settings.viewModel.GetDiscountCodeViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoveToProviderAppDialogFragment : MADialogFragment<DialogFragmentMoveToProviderAppBinding>() {

    private val viewModel by viewModels<MoveToProviderAppViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_move_to_provider_app

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(com.maproductions.mohamedalaa.shared.R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = com.maproductions.mohamedalaa.shared.R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
