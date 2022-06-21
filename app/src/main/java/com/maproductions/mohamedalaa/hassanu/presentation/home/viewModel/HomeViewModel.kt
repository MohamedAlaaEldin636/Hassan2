package com.maproductions.mohamedalaa.hassanu.presentation.home.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.*
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.NavBottomNavDirections
import com.maproductions.mohamedalaa.hassanu.presentation.auth.LogInFragment
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.presentation.bottomNav.UserBottomNavFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.home.HomeFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.home.adapters.RVServiceInHome
import com.maproductions.mohamedalaa.hassanu.presentation.home.adapters.SliderRVHomeUser
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.domain.location.LocationSelectionApiAction
import com.maproductions.mohamedalaa.shared.domain.search.SearchType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    val prefsAccount: PrefsAccount,
    gson: Gson,
    private val args: HomeFragmentArgs,
) : AndroidViewModel(application) {

    private val locationData = prefsAccount.getUserLocationData().asLiveData()

    val address = locationData.map { it?.address.orIfNullOrEmpty(myApp.getString(SR.string.unknown)) }

    val adapterSliders = SliderRVHomeUser(gson, args.isGuest)

    val adapterServices = RVServiceInHome(args.isGuest, gson)

    fun editLocation(view: View) {
        // Note location data might be null in case of guest isa.

        val locationData = locationData.value

        val (authority, paths) = if (locationData != null && locationData.latitude.isNotEmpty() && locationData.longitude.isNotEmpty()) {
            Pair(
                "com.grand.hassan.shared.location.selection.all.params",
                arrayOf(
                    locationData.latitude,
                    locationData.longitude,
                    true.toString(),
                    if (args.isGuest) LocationSelectionApiAction.NONE.name else LocationSelectionApiAction.UPDATE_USER_PROFILE.name
                )
            )
        }else {
            Pair(
                "com.grand.hassan.shared.location.selection.no.args",
                arrayOf()
            )
        }

        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            authority,
            paths = paths
        )
    }

    fun toSearchQueries(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.search.queries.is.guest",
            paths = arrayOf(SearchType.USER_HOME.name, args.isGuest.toString())
        )
    }

    fun becomeProvider(view: View) {
        view.findNavControllerOfProject().navigate(
            UserBottomNavFragmentDirections.actionDestUserBottomNavToDestMoveToProviderAppDialog()
        )
    }

}
