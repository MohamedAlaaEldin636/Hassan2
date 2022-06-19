package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName

data class ServiceInOrderDetails(
    var id: Int,
    var name: String?,
    @SerializedName("order_id") var orderId: Int,
    @SerializedName("service_id") var serviceId: Int,
    @SerializedName("category_id") var categoryId: Int?,
    var price: Float,
    var count: Int,
    var additional: Int,
) {
    val isAdditional get() = additional == 1
}
