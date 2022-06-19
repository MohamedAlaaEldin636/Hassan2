package com.maproductions.mohamedalaa.shared.presentation.location.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationData
import com.maproductions.mohamedalaa.shared.core.customTypes.MAResult
import com.maproductions.mohamedalaa.shared.core.extensions.getAddressFromLatitudeAndLongitude
import com.maproductions.mohamedalaa.shared.core.extensions.orIfNullOrEmpty
import com.maproductions.mohamedalaa.shared.core.extensions.showErrorToast
import com.maproductions.mohamedalaa.shared.core.extensions.toJson
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.domain.location.LocationSelectionApiAction
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragmentArgs
import kotlinx.coroutines.launch

@HiltViewModel
class LocationSelectionViewModel @Inject constructor(
    val args: LocationSelectionFragmentArgs,
    private val gson: Gson,
    private val repoAuth: RepoAuth
) : ViewModel() {

    val showBackButton = args.showBackButton

    val search = MutableLiveData("")

    val showMapNotSearchResults = MutableLiveData(true)

    val resultOfPlaceFragment = MutableLiveData<LatLng>()

    var myCurrentLocation: LatLng? = null

    var googleMap: GoogleMap? = null

    fun goBack(view: View) {
        view.findNavController().navigateUp()
    }

    fun toSearchPlace(view: View) {
        showMapNotSearchResults.value = false

        val fragment = view.findFragment<LocationSelectionFragment>()

        if (!Places.isInitialized()) {
            Places.initialize(view.context.applicationContext, view.context.getString(R.string.google_geo_api_key))
        }

        val autocompleteSupportFragment = (fragment.childFragmentManager
            .findFragmentById(R.id.placesFragmentContainerView) as AutocompleteSupportFragment)

        autocompleteSupportFragment.setPlaceFields(listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG
        ))

        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Timber.e("dosdkodks Places API selected place ${place.latLng}")

                resultOfPlaceFragment.postValue(place.latLng)

                showMapNotSearchResults.postValue(true)

                fragment.binding?.root?.post {
                    googleMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            place.latLng ?: return@post,
                            fragment.zoom
                        )
                    )
                }
            }

            override fun onError(status: Status) {
                Timber.e("Places API error with status $status")

                fragment.requireContext().showErrorToast(fragment.getString(R.string.something_went_wrong))

                showMapNotSearchResults.postValue(true)
            }
        })
    }

    fun moveToCurrentLocation(view: View) {
        view.findFragment<LocationSelectionFragment>().moveToCurrentLocation()
    }

    fun onSelectLocationClick(view: View) {
        val fragment = view.findFragment<LocationSelectionFragment>()

        val googleMap = googleMap ?: return

        fragment.activityViewModel?.globalLoading?.value = true

        fragment.lifecycleScope.launch {
            val address = fragment.requireContext().getAddressFromLatitudeAndLongitude(
                googleMap.cameraPosition.target.latitude,
                googleMap.cameraPosition.target.longitude
            )

            val locationData = LocationData(
                googleMap.cameraPosition.target.latitude.toString(),
                googleMap.cameraPosition.target.longitude.toString(),
                address
            )

            val result = when (args.locationSelectionApiAction) {
                LocationSelectionApiAction.UPDATE_USER_PROFILE -> {
                    repoAuth.updateUserProfile(locationData)
                }
                LocationSelectionApiAction.UPDATE_PROVIDER_PROFILE -> {
                    repoAuth.updateProviderProfile(locationData)
                }
                LocationSelectionApiAction.NONE -> null
            }

            fragment.activityViewModel?.globalLoading?.value = false

            if (result != null && result is MAResult.Failure) {
                fragment.context?.showErrorToast(
                    result.message.orIfNullOrEmpty(fragment.getString(R.string.something_went_wrong))
                )

                return@launch
            }

            delay(300)

            fragment.onBackPressedCallback?.isEnabled = false

            val navController = fragment.findNavController()

            navController.navigateUp()

            navController.currentBackStackEntry?.savedStateHandle?.set(
                LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
                locationData.toJson(gson)
            )
        }
    }

}
