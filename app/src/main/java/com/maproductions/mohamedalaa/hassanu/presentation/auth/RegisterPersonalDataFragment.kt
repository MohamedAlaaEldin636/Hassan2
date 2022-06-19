package com.maproductions.mohamedalaa.hassanu.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentLogInBinding
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentRegisterPersonalDataBinding
import com.maproductions.mohamedalaa.hassanu.presentation.auth.viewModel.LogInViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.auth.viewModel.RegisterPersonalDataViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationData
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.defaultAnimationsNavOptionsBuilder
import com.maproductions.mohamedalaa.shared.core.extensions.fromJson
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.core.extensions.popAllBackStacks
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsSplash
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragment
import com.pusher.pushnotifications.PushNotifications
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterPersonalDataFragment : MABaseFragment<FragmentRegisterPersonalDataBinding>() {

    private val viewModel by viewModels<RegisterPersonalDataViewModel>()

    @Inject
    lateinit var gson: Gson

    override fun getLayoutId(): Int = R.layout.fragment_register_personal_data

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val jsonOfLocationData = findNavController().currentBackStackEntry?.savedStateHandle
            ?.get<String>(LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON)

        if (jsonOfLocationData.isNullOrEmpty()) {
            lifecycleScope.launch {
                if (prefsSplash.getInitialLaunch().first()!! == SplashInitialLaunch.LOGIN_LOCATION) {
                    viewModel.goToLocationSelection(this@RegisterPersonalDataFragment)
                }
            }
        }else {
            val locationData = jsonOfLocationData.fromJson<LocationData>(gson)

            lifecycleScope.launch {
                prefsAccount.setUserData(
                    prefsAccount.getUserData().first()!!.copy(
                        latitude = locationData.latitude,
                        longitude = locationData.longitude,
                        address = locationData.address,
                    )
                )

                prefsSplash.setInitialLaunch(SplashInitialLaunch.MAIN)

                PusherUtils.loginBeams(
                    true,
                    prefsAccount.getApiBearerToken().first()!!,
                    prefsAccount.getUserData().first()!!.id
                )

                val navController = findNavController()

                navController.popAllBackStacks()

                navController.navigateDeepLinkWithOptions(
                    "fragment-dest",
                    "com.grand.hassan.shared.user.bottom.nav",
                )
            }
        }
    }

}
