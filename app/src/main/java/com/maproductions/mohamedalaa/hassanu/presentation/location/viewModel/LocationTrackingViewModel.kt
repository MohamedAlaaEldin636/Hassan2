package com.maproductions.mohamedalaa.hassanu.presentation.location.viewModel

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationTrackingViewModel @Inject constructor(
    //val arg: LocationTrackingFragmentArgs,
) : ViewModel() {

    val otherUserLatitude = MutableLiveData("")
    val otherUserLongitude = MutableLiveData("")

    val myLatitude = MutableLiveData("")
    val myLongitude = MutableLiveData("")

    val search = MutableLiveData("")

    val showMapNotSearchResults = MutableLiveData(true)

    var bitmapMine: Bitmap? = null
    var bitmapOther: Bitmap? = null

    fun goBack(view: View) {
        view.findNavController().navigateUp()
    }

}
