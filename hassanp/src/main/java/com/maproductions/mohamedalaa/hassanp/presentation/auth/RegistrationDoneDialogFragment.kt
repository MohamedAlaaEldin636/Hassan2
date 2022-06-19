package com.maproductions.mohamedalaa.hassanp.presentation.auth

import android.view.Gravity
import android.view.Window
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.DialogFragmentRegisterationDoneBinding
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.RegistrationDoneViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationDoneDialogFragment : MADialogFragment<DialogFragmentRegisterationDoneBinding>() {

    private val viewModel by viewModels<RegistrationDoneViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_registeration_done

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(com.maproductions.mohamedalaa.shared.R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = com.maproductions.mohamedalaa.shared.R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
