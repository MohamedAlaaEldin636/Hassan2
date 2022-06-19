package com.maproductions.mohamedalaa.shared.domain.orders

import androidx.lifecycle.MutableLiveData

data class OrdersFilter(
    val search: String? = null,
    val categoryId: Int? = null,
    val cityId: Int? = null,
)

fun OrdersFilter?.orEmpty() = this ?: OrdersFilter()
