package com.maproductions.mohamedalaa.shared.presentation.location

import com.maproductions.mohamedalaa.shared.R
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.maps.android.SphericalUtil
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationHandler
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationLatLng
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setProgressBA
import com.maproductions.mohamedalaa.shared.databinding.FragmentLocationTrackingBinding
import com.maproductions.mohamedalaa.shared.databinding.MarkerTrackingProviderBinding
import com.maproductions.mohamedalaa.shared.databinding.MarkerTrackingUserBinding
import com.maproductions.mohamedalaa.shared.domain.pusher.ResponsePusherOrder
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.viewModel.LocationTrackingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.roundToInt

@AndroidEntryPoint
class LocationTrackingFragment : MABaseFragment<FragmentLocationTrackingBinding>(),
    OnMapReadyCallback, ValueEventListener, LocationHandler.Listener {

    companion object {
        const val SAVED_STATE_PERFORM_REFRESH = "LocationTrackingFragment.SAVED_STATE_PERFORM_REFRESH"

        const val KEY_DATABASE_REFERENCE_BASE = "location"
    }

    private val database by lazy { Firebase.database }
    private val referenceProviderCurrentLocation by lazy {
        database.getReference(KEY_DATABASE_REFERENCE_BASE)
            .child("${viewModel.args.orderId}")
    }

    private val viewModel by viewModels<LocationTrackingViewModel>()

    private var googleMap: GoogleMap? = null

    /** Zoom levels https://developers.google.com/maps/documentation/android-sdk/views#zoom */
    private val zoom get() = min(googleMap?.maxZoomLevel ?: 5f, 15f)

    private var userMarker: Marker? = null

    private var providerMarker: Marker? = null

    private var movedCameraOnceToProvider = false

    @Inject
    lateinit var gson: Gson

    private val channelEvent by lazy {
        PusherUtils.getChannelEvent(
            PusherUtils.getChannelNameOnChangeOrderStatusForOrder(viewModel.args.orderId),
            PusherUtils.EVENT_NAME_ON_CHANGE_ORDER_STATUS_FOR_ORDER
        ) { data ->
            Timber.e("order status in location tracking screen data ->\n$data")

            val response = ResponsePusherOrder.fromJson(data, gson)//data.fromJson<>(gson)

            Handler(Looper.getMainLooper()).post {
                kotlin.runCatching {
                    if (response.order?.statusOfOrder != ApiOrderStatus.ON_THE_WAY) {
                        val navController = findNavControllerOfProject()

                        navController.executeWhenLoadingEndsIfExists {
                            navController.navigateUp()

                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                SAVED_STATE_PERFORM_REFRESH,
                                true
                            )
                        }
                    }
                }
            }
        }
    }

    private lateinit var locationHandler: LocationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        locationHandler = LocationHandler(
            this,
            lifecycle,
            requireContext(),
            this
        )

        super.onCreate(savedInstanceState)

        referenceProviderCurrentLocation.addValueEventListener(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_location_tracking

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelEvent.subscribe()

        locationHandler.requestCurrentLocation(true)
    }

    override fun onDestroyView() {
        locationHandler.stopLocationUpdates()

        channelEvent.unsubscribe()

        super.onDestroyView()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        drawUserOnMap(viewModel.userLocation)

        if (!movedCameraOnceToProvider) {
            if (viewModel.providerLocation != null) {
                drawProviderOnMap(viewModel.providerLocation!!)

                this.googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(viewModel.providerLocation!!, zoom)
                )
            }else {
                this.googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(viewModel.userLocation, zoom)
                )
            }
        }
    }

    override fun onChangeLocationSuccess(location: Location) {
        Handler(Looper.getMainLooper()).post {
            updateUserLocation(location.latitude, location.longitude)
        }
    }

    override fun onCurrentLocationResultSuccess(location: Location) {
        updateUserLocation(location.latitude, location.longitude)

        (childFragmentManager.findFragmentById(R.id.mapFragmentContainerView) as? SupportMapFragment)
            ?.getMapAsync(this)

        locationHandler.requestLocationUpdates()
    }

    override fun onCurrentLocationResultFailure(context: Context?, exception: Exception?) {
        executeShowingErrorOnce(
            false,
            getString(R.string.there_is_an_error_in_detecting_your_location)
        ) {
            locationHandler.requestCurrentLocation(true)
        }
    }

    override fun onChangeLocationFailure(context: Context?, exception: Exception?) {
        executeShowingErrorOnce(
            false,
            getString(R.string.there_is_an_error_in_detecting_your_location)
        ) {
            locationHandler.requestLocationUpdates()
        }
    }

    //private fun

    private fun updateUserLocation(latitude: Double, longitude: Double) {
        val distanceInMeters = SphericalUtil.computeDistanceBetween(
            LatLng(latitude, longitude),
            LatLng(viewModel.userLocation.latitude, viewModel.userLocation.longitude),
        )

        val performUpdate = distanceInMeters > 10

        if (!performUpdate) {
            return
        }

        viewModel.userLocation = LatLng(latitude, longitude)

        if (googleMap != null) {
            drawUserOnMap(viewModel.userLocation)
        }
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        if (!snapshot.exists()) {
            return
        }

        snapshot.getValue(LocationLatLng::class.java)?.also { location ->
            updateProviderLocation(location.latitude.toDouble(), location.longitude.toDouble())
        }
    }

    override fun onCancelled(error: DatabaseError) {
        requireContext().showErrorToast(getString(R.string.something_went_wrong))
    }

    private fun updateProviderLocation(latitude: Double, longitude: Double) {
        val distanceInMeters = SphericalUtil.computeDistanceBetween(
            LatLng(latitude, longitude),
            LatLng(viewModel.providerLocation?.latitude.orZero(), viewModel.providerLocation?.longitude.orZero()),
        )

        val performUpdate = distanceInMeters > 10

        if (!performUpdate) {
            return
        }

        viewModel.providerLocation = LatLng(latitude, longitude)

        if (googleMap != null) {
            drawProviderOnMap(viewModel.providerLocation!!)

            if (!movedCameraOnceToProvider) {
                movedCameraOnceToProvider = true

                this.googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(viewModel.providerLocation!!, zoom)
                )
            }
        }
    }

    @Synchronized
    private fun drawUserOnMap(location: LatLng) {
        val googleMap = googleMap ?: return

        lifecycleScope.launch {
            val bitmap = viewModel.bitmapUser ?: withContext(Dispatchers.Main) {
                val binding = MarkerTrackingUserBinding.bind(requireContext().inflateLayout(R.layout.marker_tracking_user))

                binding.root.toBitmap()!!
            }.also {
                viewModel.bitmapUser = it
            }

            userMarker?.remove()
            userMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
            )
        }
    }

    @SuppressLint("SetTextI18n")
    @Synchronized
    private fun drawProviderOnMap(location: LatLng) {
        val googleMap = googleMap ?: return

        lifecycleScope.launch {
            val bitmap = viewModel.bitmapProvider ?: withContext(Dispatchers.Main) {
                val size = requireContext().dpToPx(24f).roundToInt()

                val bitmap = Glide.with(this@LocationTrackingFragment)
                    .asBitmap()
                    .load(viewModel.args.imageUrl)
                    .apply(RequestOptions().override(size, size))
                    .intoBitmap()

                val binding = MarkerTrackingProviderBinding.bind(requireContext().inflateLayout(R.layout.marker_tracking_provider))

                bitmap?.also { binding.providerImageView.setImageBitmap(bitmap) }

                binding.providerNameTextView.text = viewModel.args.name
                binding.providerRatingBar.setProgressBA(
                    (viewModel.args.averageRate.orZero() * 20f).roundToInt()
                )
                binding.providerRateTextView.text = "(${viewModel.args.averageRate.orZero().roundHalfUpToIntOrFloat(1)})"

                binding.root.toBitmap()!!
            }.also {
                viewModel.bitmapProvider = it
            }

            providerMarker?.remove()
            providerMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .anchor(0.5f, 0f)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
            )
        }
    }

}
