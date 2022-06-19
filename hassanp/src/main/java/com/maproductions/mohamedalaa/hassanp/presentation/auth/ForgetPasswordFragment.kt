package com.maproductions.mohamedalaa.hassanp.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentForgetPasswordBinding
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentLogInBinding
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.ForgetPasswordViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.LogInViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.auth.ProviderData
import com.maproductions.mohamedalaa.shared.domain.auth.ResponseVerifyCode
import com.maproductions.mohamedalaa.shared.domain.local.preferences.UserData
import com.maproductions.mohamedalaa.shared.domain.location.LocationSelectionApiAction
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.maproductions.mohamedalaa.shared.presentation.auth.VerifyPhoneFragment
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ForgetPasswordFragment : MABaseFragment<FragmentForgetPasswordBinding>() {

    private val viewModel by viewModels<ForgetPasswordViewModel>()

    @Inject
    lateinit var gson: Gson

    override fun getLayoutId(): Int = R.layout.fragment_forget_password

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            VerifyPhoneFragment.SAVED_STATE_VERIFICATION_RESPONSE_AS_JSON,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            val response = it.fromJson<ResponseVerifyCode>(gson)

            lifecycleScope.launch {
                prefsAccount.setProviderData(
                    ProviderData(
                        response.id.orZero(),
                        response.typeOfAccount.orEmpty(),
                        response.verified.orZero(),
                        name = response.name,
                        email = response.email,
                        phone = response.phone,
                        latitude = response.latitude,
                        longitude = response.longitude,
                        address = response.address,
                        apiToken = response.apiToken.orEmpty(),
                        imageUrl = response.imageUrl,
                        ordersFlag = response.ordersFlag.orZero(),
                        services = response.services,
                        category = response.category,
                    )
                )

                findNavControllerOfProject().navigate(
                    ForgetPasswordFragmentDirections.actionDestForgetPasswordToDestNewPassword(
                        viewModel.phone.value.orEmpty()
                    )
                )
            }
        }
    }

}
