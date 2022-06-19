package com.maproductions.mohamedalaa.shared.core.customTypes

import com.maproductions.mohamedalaa.shared.data.api.ApiConst

enum class AccountType(val apiValue: String) {
    USER(ApiConst.Query.USER),
    PROVIDER(ApiConst.Query.PROVIDER),
}