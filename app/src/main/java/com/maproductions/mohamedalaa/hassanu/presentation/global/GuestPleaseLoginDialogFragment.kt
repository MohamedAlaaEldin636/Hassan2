package com.maproductions.mohamedalaa.hassanu.presentation.global

import android.view.Gravity
import android.view.Window
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentAcceptedProviderServiceRequestBinding
import com.maproductions.mohamedalaa.hassanu.databinding.DialogFragmentGuestPleaseLoginBinding
import com.maproductions.mohamedalaa.hassanu.presentation.global.viewModel.GuestPleaseLoginViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.AcceptedProviderServiceRequestViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.getMyDrawable
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuestPleaseLoginDialogFragment : MADialogFragment<DialogFragmentGuestPleaseLoginBinding>() {

    private val viewModel by viewModels<GuestPleaseLoginViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_guest_please_login

    override val windowGravity: Int = Gravity.BOTTOM

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(com.maproductions.mohamedalaa.shared.R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = com.maproductions.mohamedalaa.shared.R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
