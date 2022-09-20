package com.maproductions.mohamedalaa.hassanu.presentation.auth.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.presentation.auth.LogInFragment
import com.maproductions.mohamedalaa.hassanu.presentation.auth.LogInFragmentDirections
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.domain.settings.ImageWithTextAndTitleFlag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import com.maproductions.mohamedalaa.shared.R as SR

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val repoAuth: RepoAuth,
    private val prefsAccount: PrefsAccount,
) : ViewModel() {

    val phone = MutableLiveData("")

    val agreeOnTermsAndConditions = MutableLiveData(false)

    fun skip(view: View) {
        view.findNavController().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.user.bottom.nav.is.guest",
            paths = arrayOf(true.toString())
        )
    }

    fun toggleAgreeOnTermsAndConditions() = agreeOnTermsAndConditions.toggleValueIfNotNull()

    fun goToTermsAndConditions(view: View) {
        view.findNavController().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.image.with.text.and.title",
            paths = arrayOf(ImageWithTextAndTitleFlag.USER_TERMS_AND_CONDITIONS.name)
        )
    }

    fun logIn(view: View) {
        if (phone.value.isNullOrEmpty()) {
            return view.context.showErrorToast(view.context.getString(SR.string.field_required))
        }

        if (!phone.value.isValidIraqPhoneWithoutPrefix964()) {
            return view.context.showErrorToast(view.context.getString(SR.string.phone_number_is_wrong))
        }

        // https://en.wikipedia.org/wiki/Telephone_numbers_in_Iraq

        if (agreeOnTermsAndConditions.value != true) {
            return view.context.showErrorToast(view.context.getString(SR.string.must_accept_terms_and_conditions))
        }

        view.findFragment<LogInFragment>().executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            canCancelDialog = true,
            afterShowingLoading = {
                repoAuth.login(phone.value.orEmpty())
            },
            afterHidingLoading = {
                // Always go to verify code then request name & email then location then
                // prefs splash can be MAIN

                view.findNavController().navigateDeepLinkWithOptions(
                    "fragment-dest",
                    "com.grand.hassan.shared.verify.phone",
                    paths = arrayOf(true.toString(), phone.value.orEmpty())
                )
            },
        )
    }

    /*
    <com.facebook.login.widget.LoginButton
    android:id="@+id/login_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    android:layout_marginTop="30dp"
    android:layout_marginBottom="30dp" />
    */
    fun onFacebookClick(view: View) {
        /*
         AccessToken.getCurrentAccessToken() and Profile.getCurrentProfile().
         */
        LoginManager.getInstance().logOut()

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        val profile = Profile.getCurrentProfile()

        Timber.e("hsiuaha facebook isLoggedIn $isLoggedIn, $profile")

        val fragment = view.findFragment<LogInFragment>()

        LoginManager.getInstance().logInWithReadPermissions(
            fragment,
            fragment.callbackManager,
            setOf("public_profile")
        )


        /*val callbackManager = CallbackManager.Factory.create()

        val loginManager = LoginManager.getInstance()

        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Timber.e("hsiuaha facebook id ${result.accessToken.userId}, accessToken -> ${result.accessToken}")

                // todo see osama when done facebook login isa. and 3ando how to get name and email isa.
                //https://stackoverflow.com/questions/41861564/server-error-code-1675030-message-error-performing-query
                //performSocialLoginWithApi(view.findFragment(), result.accessToken.userId)
            }

            override fun onError(error: FacebookException) {
                Timber.e("hsiuaha Facebook Exception 1 $error")
                Timber.e("hsiuaha Facebook Exception 1.5 ${error.message}")
                Timber.e("hsiuaha Facebook Exception 1.3 ${error.localizedMessage}")
                Timber.e("hsiuaha Facebook Exception 1.2 ${error.suppressed.toList()}")
                Timber.e("hsiuaha Facebook Exception 1.0 ${error.stackTrace.toList()}")
                Timber.e("hsiuaha Facebook Exception 1.1 ${error.cause}")
                view.context.showErrorToast(view.context.getString(SR.string.something_went_wrong))
            }

            override fun onCancel() {
                Timber.e("hsiuaha facebook onCancel 0")
                view.context.showErrorToast(view.context.getString(SR.string.something_went_wrong))
            }
        })

        loginManager.logIn(
            view.findFragment<LogInFragment>().requireActivity(),
            callbackManager,
            listOf("email", "public_profile"*//*, "user_friends"*//*),
        )*/
    }

    fun onGoogleClick(view: View) {
        val fragment = view.findFragment<LogInFragment>()

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestEmail()
            .requestProfile()
            .build()
        val client = GoogleSignIn.getClient(view.context, options)

        //val account = GoogleSignIn.getLastSignedInAccount(view.context)

        fragment.activityResultGoogleSignIn.launch(client.signInIntent)
    }

    fun performSocialLoginWithApi(fragment: LogInFragment, socialId: String, name: String?, email: String?) {
        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                Timber.e("socialId $socialId, name $name, email $email")

                repoAuth.checkSocialLogin(socialId)
            },
            afterHidingLoading = {
                if (it == null) {
                    fragment.findNavControllerOfProject().navigate(
                        LogInFragmentDirections.actionDestLogInToDestRegisterPhone(
                            socialId, name, email
                        )
                    )
                }else {
                    if (it.name.isNullOrEmpty() && !name.isNullOrEmpty()) {
                        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
                            afterShowingLoading = {
                                prefsAccount.setApiBearerToken(it.apiToken.orEmpty())

                                repoAuth.updateUserProfile(name, email)
                            },
                            afterHidingLoading = { response2 ->
                                val navController = fragment.findNavControllerOfProject()

                                if (!it.isVerified) {
                                    navController.navigateDeepLinkWithOptions(
                                        "fragment-dest",
                                        "com.grand.hassan.shared.verify.phone",
                                        paths = arrayOf(true.toString(), response2?.phone.orEmpty()/*phone.value.orEmpty()*/)
                                    )
                                }else {
                                    viewModelScope.launch {
                                        prefsAccount.setApiBearerToken(it.apiToken.orEmpty())

                                        fragment.handleReponseVerifyCode(
                                            it.copy(name = name, email = email)
                                        )
                                    }
                                }
                            }
                        )
                    }else {
                        viewModelScope.launch {
                            prefsAccount.setApiBearerToken(it.apiToken.orEmpty())

                            fragment.handleReponseVerifyCode(it)
                        }
                    }
                }
            }
        )
    }

}
