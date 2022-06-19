package com.maproductions.mohamedalaa.hassanp.presentation.home

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.DialogFragmentNewOrderBinding
import com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel.NewOrderViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.core.extensions.removeAllViewsInstanceOf
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewOrderDialogFragment : MADialogFragment<DialogFragmentNewOrderBinding>() {

    private val viewModel by viewModels<NewOrderViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_new_order

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(com.maproductions.mohamedalaa.shared.R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = com.maproductions.mohamedalaa.shared.R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.chipGroup?.removeAllViewsInstanceOf<Chip>()
        for (service in viewModel.services) {
            val chip = Chip(context).also {
                it.text = service.name.orEmpty()
            }

            chip.setChipBackgroundColorResource(com.maproductions.mohamedalaa.shared.R.color.chip_themed_bg_color)

            binding?.chipGroup?.addView(chip)
        }
    }

}
