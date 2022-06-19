package com.maproductions.mohamedalaa.hassanu.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentSettingsBinding
import com.maproductions.mohamedalaa.hassanu.presentation.settings.viewModel.SettingsViewModel
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.databinding.FragmentChatDetailsBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.chat.viewModel.ChatDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class SettingsFragment : MABaseFragment<FragmentSettingsBinding>() {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}
