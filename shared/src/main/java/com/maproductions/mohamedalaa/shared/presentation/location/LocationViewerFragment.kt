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
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.showErrorToast
import com.maproductions.mohamedalaa.shared.core.extensions.showNormalToast
import com.maproductions.mohamedalaa.shared.core.extensions.toBitmap
import com.maproductions.mohamedalaa.shared.databinding.FragmentLocationSelectionBinding
import com.maproductions.mohamedalaa.shared.databinding.FragmentLocationViewerBinding
import com.maproductions.mohamedalaa.shared.databinding.MarkerTrackingUserBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.viewModel.LocationSelectionViewModel
import com.maproductions.mohamedalaa.shared.presentation.location.viewModel.LocationViewerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.math.min

@AndroidEntryPoint
class LocationViewerFragment : MABaseFragment<FragmentLocationViewerBinding>(),
    OnMapReadyCallback {

    companion object {
        const val KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON = "KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON"
    }

    private val viewModel by viewModels<LocationViewerViewModel>()

    /** Zoom levels https://developers.google.com/maps/documentation/android-sdk/views#zoom */
    private val zoom get() = min(viewModel.googleMap?.maxZoomLevel ?: 5f, 15f)

    override fun getLayoutId(): Int = R.layout.fragment_location_viewer

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

        val location = LatLng(
            viewModel.args.latitude.toDouble(),
            viewModel.args.longitude.toDouble()
        )

        drawOnMap(location)

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(location, zoom)
        )
    }

    @Synchronized
    private fun drawOnMap(location: LatLng) {
        val googleMap = viewModel.googleMap ?: return

        lifecycleScope.launch {
            val bitmap = viewModel.bitmap ?: withContext(Dispatchers.Main) {
                val binding = MarkerTrackingUserBinding.bind(requireContext().inflateLayout(R.layout.marker_tracking_user))

                binding.root.toBitmap()!!
            }.also {
                viewModel.bitmap = it
            }

            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
            )
        }
    }

}
