package com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServiceDateAndTimeSelectionFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServiceImageAndDescriptionSelectionFragment
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServiceImageAndDescriptionSelectionFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServiceImageAndDescriptionSelectionFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.settings.PersonalDataFragment
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.extensions.dpToPx
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import com.maproductions.mohamedalaa.shared.core.extensions.toJson
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.presentation.base.adapters.RVItemImageRect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ServiceImageAndDescriptionSelectionViewModel @Inject constructor(
    application: Application,
    val args: ServiceImageAndDescriptionSelectionFragmentArgs,
    private val gson: Gson
) : AndroidViewModel(application) {

    val adapter = RVItemImageRect()

    val description = MutableLiveData("")

    fun uploadImages(view: View) {
        view.findFragment<ServiceImageAndDescriptionSelectionFragment>().pickImageOrRequestPermissions()
    }

    fun next(view: View) {
        // uri = Uri.parse(stringUri); // stringUri = uri.toString();
        val imagesUrisAsStrings = adapter.currentList.toList().filterIsInstance<MAImage.IUri>()
            .map { it.uri.toString() }

        view.findNavController().navigate(
            ServiceImageAndDescriptionSelectionFragmentDirections.actionDestServiceImageAndDescriptionSelectionToDestServiceConfirmation(
                args.categoryId,
                args.categoryName,
                args.jsonOfServices,
                args.addressId,
                args.orderedAt,
                args.orderType,
                if (imagesUrisAsStrings.isEmpty()) "" else imagesUrisAsStrings.toJson(gson),
                description.value.orEmpty(),
                args.jsonDeliveryData
            )
        )
    }

}
