package com.maproductions.mohamedalaa.shared.domain.notifications

enum class NotificationType(val apiValue: String) {
    ADMIN("admin"),
    ORDER("order"),
    WALLET("wallet"),
    CONFIRM_ADDITIONAL_SERVICES("confirm-additional-services"),
    MESSAGE("message"),
}
