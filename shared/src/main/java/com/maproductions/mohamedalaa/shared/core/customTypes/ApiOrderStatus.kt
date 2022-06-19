package com.maproductions.mohamedalaa.shared.core.customTypes

enum class ApiOrderStatus(val apiValue: String) {
    ACCEPTED("accepted"),
    ON_THE_WAY("on-the-way"),
    ARRIVED("arrived"),
    WORK_STARTED("work-started"),
    FINISHED("finished"),
    REJECTED("rejected"),
    CANCELLED("cancelled"),
    PENDING("pending"),
}
