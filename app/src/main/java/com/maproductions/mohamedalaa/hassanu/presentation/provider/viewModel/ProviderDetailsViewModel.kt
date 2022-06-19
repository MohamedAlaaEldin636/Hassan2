package com.maproductions.mohamedalaa.hassanu.presentation.provider.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.presentation.provider.ProviderDetailsFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.provider.adapter.RVItemPersonRate
import com.maproductions.mohamedalaa.shared.core.customTypes.ImageOrVideo
import com.maproductions.mohamedalaa.shared.core.extensions.fromJson
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.core.extensions.roundHalfUpToIntOrFloat
import com.maproductions.mohamedalaa.shared.core.extensions.toIntOrFloat
import com.maproductions.mohamedalaa.shared.domain.orders.ProviderInOrder
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemImageOrVideo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ProviderDetailsViewModel @Inject constructor(
    application: Application,
    args: ProviderDetailsFragmentArgs,
    gson: Gson,
) : AndroidViewModel(application) {

    val provider = args.jsonOfProviderInOrder.fromJson<ProviderInOrder>(gson)

    val imageUrl = provider.imageUrl

    val name = provider.name
    val rateProgressOfHundred = (provider.averageRate.orZero() * 20f).roundToInt()
    val rateText = "(${provider.averageRate.orZero().roundHalfUpToIntOrFloat(1)})"

    val summary = provider.description

    val previousWorks = provider.previousWorks.map { ImageOrVideo(it.fileUrl, it.typeOfFile) }
    val adapterPreviousWorks = RVItemImageOrVideo(previousWorks)

    val reviews = provider.reviews?.data.orEmpty()
    val adapterReviews = RVItemPersonRate(reviews)

}
