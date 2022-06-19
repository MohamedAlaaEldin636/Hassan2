package com.maproductions.mohamedalaa.hassanp.presentation.myAccount

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentMyAccountBinding
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.MyAccountViewModel
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAccountFragment : MABaseFragment<FragmentMyAccountBinding>() {

    private val viewModel by viewModels<MyAccountViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_my_account

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}
