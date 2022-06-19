package com.maproductions.mohamedalaa.shared.presentation.location.viewModel

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.android.gms.maps.GoogleMap
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.location.LocationViewerFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewerViewModel @Inject constructor(
    val args: LocationViewerFragmentArgs,
) : ViewModel() {

    var googleMap: GoogleMap? = null

    var bitmap: Bitmap? = null

    fun goBack(view: View) {
        view.findNavController().navigateUp()
    }

}
