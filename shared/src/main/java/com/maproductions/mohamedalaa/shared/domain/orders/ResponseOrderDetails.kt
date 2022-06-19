package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.OrderStatus
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress

data class ResponseOrderDetails(
    var id: Int,
    @SerializedName("ordered_at_date") var orderedAtDate: String,
    @SerializedName("ordered_at_time") var orderedAtTime: String,
    var total: Float,
    @SerializedName("delivery_cost") var deliveryCost: Float,
    @SerializedName("extra_notes") var extraNotes: String?,
    var address: ResponseAddress?,
    var provider: ProviderInOrder?,
    var user: UserInOrder,
    @SerializedName("status") var orderStatus: String,
    @SerializedName("images") var imagesUrls: List<PreviousWorkInProvider>,
    @SerializedName("cancellation_fees") var cancellationFeesPercent: Float,
    var services: List<ServiceInOrderDetails>,
    @SerializedName("order_min_price_for_extra") var orderMinPriceForExtra: Float?,
) {
    val statusOfOrder get() = ApiOrderStatus.values().first { it.apiValue == orderStatus }
}
