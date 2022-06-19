package com.maproductions.mohamedalaa.hassanu.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentRegisterPersonalDataBinding
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentRegisterPhoneBinding
import com.maproductions.mohamedalaa.hassanu.presentation.auth.viewModel.RegisterPersonalDataViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.auth.viewModel.RegisterPhoneViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationData
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.maproductions.mohamedalaa.shared.presentation.auth.VerifyPhoneFragment
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterPhoneFragment : MABaseFragment<FragmentRegisterPhoneBinding>() {

    private val viewModel by viewModels<RegisterPhoneViewModel>()

    @Inject
    lateinit var gson: Gson

    override fun getLayoutId(): Int = R.layout.fragment_register_phone

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}
