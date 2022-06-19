package com.maproductions.mohamedalaa.shared.domain.home

import androidx.annotation.IntRange

data class ServiceInCategoryWithCount(
    var serviceInCategory: ServiceInCategory,
    @IntRange(from = 1) var count: Int,
)
