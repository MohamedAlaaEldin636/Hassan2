package com.maproductions.mohamedalaa.hassanp.presentation.auth

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentLogInBinding
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.LogInViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationData
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment : MABaseFragment<FragmentLogInBinding>() {

    private val viewModel by viewModels<LogInViewModel>()

    @Inject
    lateinit var gson: Gson

    override fun getLayoutId(): Int = R.layout.fragment_log_in

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            val locationData = it.fromJson<LocationData>(gson)

            lifecycleScope.launch {
                val provider = prefsAccount.getProviderData().first()!!.copy(
                    latitude = locationData.latitude,
                    longitude = locationData.longitude,
                    address = locationData.address,
                )

                prefsAccount.setProviderData(provider)

                prefsSplash.setInitialLaunch(
                    if (provider.isSuspendedAccount) {
                        SplashInitialLaunch.PROVIDER_ACCOUNT_SUSPENDED
                    }else {
                        SplashInitialLaunch.MAIN
                    }
                )

                PusherUtils.loginBeams(
                    false,
                    prefsAccount.getApiBearerToken().first()!!,
                    prefsAccount.getProviderData().first()!!.id
                )

                findNavController().navigateDeepLinkWithOptions(
                    "fragment-dest",
                    "com.grand.hassan.shared.provider.bottom.nav.suspend.account",
                    defaultAnimationsNavOptionsBuilder()
                        .setPopUpTo(R.id.dest_log_in, true)
                        .build(),
                    provider.isSuspendedAccount.toString()
                )
            }
        }
    }

}
