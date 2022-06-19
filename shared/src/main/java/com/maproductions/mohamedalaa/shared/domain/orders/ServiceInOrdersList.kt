package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName

data class ServiceInOrdersList(
    var id: Int,
    @SerializedName("order_id") var orderId: Int,
    @SerializedName("service_id") var serviceId: Int,
    var price: Float,
    var count: Int,
    var additional: Int,
    var name: String?,
) {
    val isAdditional get() = additional == 1
}

/*
"services": [
                    {
                        "id": 6,
                        "order_id": 4,
                        "service_id": 1,
                        "price": 10,
                        "count": 2,
                        "additional": 0
                    }
                ]
 */
