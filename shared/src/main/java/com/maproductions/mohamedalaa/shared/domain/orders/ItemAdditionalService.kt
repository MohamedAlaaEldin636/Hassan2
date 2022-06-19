package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName

data class ItemAdditionalService(
    var id: Int?,
    @SerializedName("category_id") var categoryId: Int?,
    //var active: Int?,
    var price: Float?,
    var name: String?,
    var count: Int?,
)

/*
{
                    "id": 1,
                    "category_id": 1,
                    "active": 1,
                    "price": 120,
                    "name": "الدهان"
                }
 */
