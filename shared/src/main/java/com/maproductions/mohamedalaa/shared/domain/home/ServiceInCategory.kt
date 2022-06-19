package com.maproductions.mohamedalaa.shared.domain.home

import com.google.gson.annotations.SerializedName

data class ServiceInCategory(
    var id: Int,
    @SerializedName("category_id") var categoryId: Int,
    var active: Int?,
    var price: Float,
    var name: String?,
) {
    val isActive get() = active == 1
}
/*
{
                    "id": 1,
                    "category_id": 1,
                    "active": 1,
                    "price": 120,
                    "name": "الدهان"
                }
 */
