package com.maproductions.mohamedalaa.shared.presentation.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.maproductions.mohamedalaa.shared.core.extensions.showNormalToast
import com.maproductions.mohamedalaa.shared.databinding.FragmentLocationSelectionBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.viewModel.LocationSelectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationHandler
import com.maproductions.mohamedalaa.shared.core.extensions.showErrorToast
import timber.log.Timber

@AndroidEntryPoint
class LocationSelectionFragment : MABaseFragment<FragmentLocationSelectionBinding>(),
    OnMapReadyCallback, LocationHandler.Listener {

    companion object {
        const val KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON = "KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON"
    }

    private val viewModel by viewModels<LocationSelectionViewModel>()

    private lateinit var locationHandler: LocationHandler

    /** Zoom levels https://developers.google.com/maps/documentation/android-sdk/views#zoom */
    val zoom get() = min(viewModel.googleMap?.maxZoomLevel ?: 5f, 15f)

    var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        locationHandler = LocationHandler(
            this,
            lifecycle,
            requireContext(),
            this
        )

        super.onCreate(savedInstanceState)

        if (!viewModel.args.showBackButton) {
            onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this, true) {
                // Do Nothing.
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_location_selection

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup map
        (childFragmentManager.findFragmentById(R.id.mapFragmentContainerView) as? SupportMapFragment)
            ?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.googleMap = googleMap

        Timber.e("onMapReady -> ${viewModel.args.latitude}")

        if (viewModel.args.latitude == null || viewModel.args.longitude == null) {
            moveToCurrentLocation()
        }

        val location = LatLng(
            viewModel.args.latitude?.toDouble() ?: 0.0,
            viewModel.args.longitude?.toDouble() ?: 0.0
        )

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                viewModel.resultOfPlaceFragment.value ?: location,
                zoom
            )
        )
    }

    fun moveToCurrentLocation() {
        viewModel.myCurrentLocation?.also { myCurrentLocation ->
            viewModel.googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(myCurrentLocation, zoom)
            )
        } ?: locationHandler.requestCurrentLocation(true)
    }

    override fun onCurrentLocationResultSuccess(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)

        viewModel.myCurrentLocation = latLng

        viewModel.googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

}
