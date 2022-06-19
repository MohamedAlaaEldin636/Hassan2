package com.maproductions.mohamedalaa.shared.presentation.location.viewModel

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.maproductions.mohamedalaa.shared.presentation.location.LocationTrackingFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.location.LocationViewerFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationTrackingViewModel @Inject constructor(
    val args: LocationTrackingFragmentArgs,
) : ViewModel() {

    var providerLocation: LatLng? = null

    var userLocation = LatLng(args.latitude.toDouble(), args.longitude.toDouble())

    var bitmapUser: Bitmap? = null
    var bitmapProvider: Bitmap? = null

    fun goBack(view: View) {
        view.findNavController().navigateUp()
    }

}
