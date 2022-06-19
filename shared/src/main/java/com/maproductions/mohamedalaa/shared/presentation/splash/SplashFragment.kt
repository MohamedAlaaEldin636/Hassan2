package com.maproductions.mohamedalaa.shared.presentation.splash

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.maproductions.mohamedalaa.shared.BuildConfig
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.SharedApplication
import com.maproductions.mohamedalaa.shared.core.customTypes.StaticValues
import com.maproductions.mohamedalaa.shared.core.extensions.defaultAnimationsNavOptionsBuilder
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.databinding.FragmentSplashBinding
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import com.pusher.pushnotifications.PushNotifications
import kotlin.coroutines.resume


@AndroidEntryPoint
class SplashFragment : MABaseFragment<FragmentSplashBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // https://www.youtube.com/watch?v=fSB6_KE95bU&t=746s
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (BuildConfig.DEBUG) {
                    Timber.e("SplashFragment api token -> ${prefsAccount.getApiBearerToken().first()}")
                    Timber.e("SplashFragment user -> ${prefsAccount.getUserData().first()}")
                    Timber.e("SplashFragment provider -> ${prefsAccount.getProviderData().first()}")
                }

                delay(1_000)

                when (prefsSplash.getInitialLaunch().first()) {
                    SplashInitialLaunch.MAIN -> {
                        val authority = if ((requireActivity().applicationContext as SharedApplication).isUserNotProvider) {
                            "com.grand.hassan.shared.user.bottom.nav"
                        }else {
                            "com.grand.hassan.shared.provider.bottom.nav"
                        }

                        findNavController().navigateDeepLinkWithOptions(
                            "fragment-dest",
                            authority,
                            defaultAnimationsNavOptionsBuilder()
                                .setPopUpTo(R.id.dest_splash, true)
                                .build()
                        )
                    }
                    SplashInitialLaunch.LOGIN_LOCATION -> {
                        view.findNavController().navigateDeepLinkWithOptions(
                            "fragment-dest",
                            "com.grand.hassan.shared.registration.almost.complete.redirect.to.location",
                            defaultAnimationsNavOptionsBuilder()
                                .setPopUpTo(R.id.dest_splash, true)
                                .build()
                        )
                    }
                    SplashInitialLaunch.LOGIN -> {
                        view.findNavController().navigateDeepLinkWithOptions(
                            "fragment-dest",
                            "com.grand.hassan.shared.log.in",
                            defaultAnimationsNavOptionsBuilder()
                                .setPopUpTo(R.id.dest_splash, true)
                                .build()
                        )
                    }
                    else /* SplashInitialLaunch.ON_BOARD, null */ -> {
                        findNavController().navigate(SplashFragmentDirections.actionDestSplashToDestOnBoard())
                    }
                }
            }
        }
    }

}
