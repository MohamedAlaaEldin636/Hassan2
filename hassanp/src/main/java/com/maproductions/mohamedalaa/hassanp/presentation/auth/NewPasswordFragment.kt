package com.maproductions.mohamedalaa.hassanp.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentForgetPasswordBinding
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentNewPasswordBinding
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.ForgetPasswordViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.NewPasswordViewModel
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPasswordFragment : MABaseFragment<FragmentNewPasswordBinding>() {

    private val viewModel by viewModels<NewPasswordViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_new_password

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
