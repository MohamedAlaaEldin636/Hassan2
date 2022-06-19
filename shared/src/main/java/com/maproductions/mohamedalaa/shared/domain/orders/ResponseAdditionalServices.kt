package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName

data class ResponseAdditionalServices(
    var id: Int?,

    @SerializedName("total") var sumOfOriginalServices: Float?,
    @SerializedName("additional_services_sum") var sumOfAdditionalServices: Float?,
    @SerializedName("total_services_sum") var sumOfAllServices: Float?,

    @SerializedName("additional_services") var additionalServices: List<ItemAdditionalService>?,

    @SerializedName("order_number") var orderNumber: String?,
)

/*
{
    "code": 200,
    "message": "The action ran successfully!",
    "data": {
            "id": 67,
            "total": 760,
            "additional_services": [
                {
                    "id": 1,
                    "category_id": 1,
                    "active": 1,
                    "price": 120,
                    "name": "الدهان"
                }
            ],
            "additional_services_sum": 120,
            "total_services_sum": 880
        }
    }
 */