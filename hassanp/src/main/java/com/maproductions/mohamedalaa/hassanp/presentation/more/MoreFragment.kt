package com.maproductions.mohamedalaa.hassanp.presentation.more

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentMoreBinding
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentMyAccountBinding
import com.maproductions.mohamedalaa.hassanp.presentation.more.viewModel.MoreViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.MyAccountViewModel
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : MABaseFragment<FragmentMoreBinding>() {

    private val viewModel by viewModels<MoreViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_more

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}
