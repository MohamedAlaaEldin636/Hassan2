package com.maproductions.mohamedalaa.hassanu.presentation.auth

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentLogInBinding
import com.maproductions.mohamedalaa.hassanu.presentation.auth.viewModel.LogInViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationData
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.auth.ResponseVerifyCode
import com.maproductions.mohamedalaa.shared.domain.local.preferences.UserData
import com.maproductions.mohamedalaa.shared.domain.location.LocationSelectionApiAction
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.maproductions.mohamedalaa.shared.presentation.auth.VerifyPhoneFragment
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.maproductions.mohamedalaa.shared.R as SR


@AndroidEntryPoint
class LogInFragment : MABaseFragment<FragmentLogInBinding>() {

    private val viewModel by viewModels<LogInViewModel>()

    @Inject
    protected lateinit var gson: Gson

    lateinit var callbackManager: CallbackManager

    val activityResultGoogleSignIn = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        Timber.e("it.resultCode == Activity.RESULT_OK ${it.resultCode} == ${Activity.RESULT_OK}")

        if (it.resultCode == Activity.RESULT_OK) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

            try {
                val account = task.getResult(ApiException::class.java)

                val id = account.id!!

                Timber.e("id is $id")

                viewModel.performSocialLoginWithApi(this, id, account.displayName, account.email)
            }catch (throwable: Throwable) {
                Timber.e("Can't login with google -> $throwable")

                requireContext().showErrorToast(getString(SR.string.something_went_wrong))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Timber.e("hsiuaha facebook id ${result.accessToken.userId}, accessToken -> ${result.accessToken}")
                }

                override fun onCancel() {
                    Timber.e("hsiuaha facebook cancel")
                }

                override fun onError(error: FacebookException) {
                    Timber.e("hsiuaha facebook error")

                    Timber.e("hsiuaha Facebook Exception 1 $error")
                    Timber.e("hsiuaha Facebook Exception 1.5 ${error.message}")
                    Timber.e("hsiuaha Facebook Exception 1.3 ${error.localizedMessage}")
                    Timber.e("hsiuaha Facebook Exception 1.2 ${error.suppressed.toList()}")
                    Timber.e("hsiuaha Facebook Exception 1.0 ${error.stackTrace.toList().joinToString("\n")}")
                    Timber.e("hsiuaha Facebook Exception 1.1 ${error.cause}")

                    context?.showErrorToast(getString(SR.string.something_went_wrong))
                }
            }
        )
    }

    override fun getLayoutId(): Int = R.layout.fragment_log_in

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

            Timber.e("response -> $response")

            handleReponseVerifyCode(response)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            val locationData = it.fromJson<LocationData>(gson)

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

                findNavController().navigateDeepLinkWithOptions(
                    "fragment-dest",
                    "com.grand.hassan.shared.user.bottom.nav",
                    defaultAnimationsNavOptionsBuilder()
                        .setPopUpTo(R.id.dest_log_in, true)
                        .build()
                )
            }
        }
    }

    fun handleReponseVerifyCode(response: ResponseVerifyCode) {
        when {
            response.name.isNullOrEmpty() -> {
                lifecycleScope.launch {
                    prefsAccount.setUserData(
                        UserData(
                            id = response.id.orZero(),
                            phone = response.phone.orEmpty(),
                            imageUrl = response.imageUrl.orEmpty(),
                        )
                    )

                    findNavController().navigate(
                        LogInFragmentDirections.actionDestLogInToDestRegisterPersonalData()
                    )
                }
            }
            response.latitude.isNullOrEmpty() -> {
                lifecycleScope.launch {
                    prefsAccount.setUserData(
                        UserData(
                            id = response.id.orZero(),
                            name = response.name.orEmpty(),
                            email = response.email.orEmpty(),
                            phone = response.phone.orEmpty(),
                            imageUrl = response.imageUrl.orEmpty(),
                        )
                    )

                    prefsSplash.setInitialLaunch(SplashInitialLaunch.LOGIN_LOCATION)

                    findNavController().navigateDeepLinkWithOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.location.selection.back.button.and.api.action",
                        paths = arrayOf(false.toString(), LocationSelectionApiAction.UPDATE_USER_PROFILE.name)
                    )
                }
            }
            else -> {
                lifecycleScope.launch {
                    prefsAccount.setUserData(
                        UserData(
                            response.id.orZero(),
                            response.name.orEmpty(),
                            response.email.orEmpty(),
                            response.latitude.orEmpty(),
                            response.longitude.orEmpty(),
                            response.address.orEmpty(),
                            response.phone.orEmpty(),
                            response.imageUrl.orEmpty(),
                        )
                    )

                    prefsSplash.setInitialLaunch(SplashInitialLaunch.MAIN)

                    PusherUtils.loginBeams(
                        true,
                        prefsAccount.getApiBearerToken().first()!!,
                        response.id.orZero()
                    )

                    findNavController().popAllBackStacks()

                    findNavController().navigateDeepLinkWithOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.user.bottom.nav",
                        defaultAnimationsNavOptionsBuilder()
                            .build(),
                    )
                }
            }
        }
    }

}
