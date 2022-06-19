package com.maproductions.mohamedalaa.shared.domain.orders

import androidx.annotation.FloatRange
import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging

data class ProviderInOrder(
    var id: Int,
    var name: String,
    var phone: String,
    @SerializedName("image") var imageUrl: String,
    @FloatRange(from = 0.0, to = 5.0) @SerializedName("average_rate") var averageRate: Float,
    @SerializedName("rate_count") var rateCount: Int,
    var description: String,
    @SerializedName("previous_work") var previousWorks: List<PreviousWorkInProvider>,
    var reviews: MABasePaging<ReviewInProvider>?,
)
