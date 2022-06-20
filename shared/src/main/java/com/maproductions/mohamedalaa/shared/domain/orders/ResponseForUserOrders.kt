package com.maproductions.mohamedalaa.shared.domain.orders

import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging

data class ResponseForUserOrders(
    var orders: MABasePaging<ResponseOrder>?,
)
