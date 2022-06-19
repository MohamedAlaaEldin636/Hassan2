package com.maproductions.mohamedalaa.shared.domain.wallet

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging

data class ResponseWallet(
    @SerializedName("wallet") var currentAmount: Float,
    @SerializedName("wallet_limit") var limit: Float,
    var history: MABasePaging<ItemWallet>,
)

/*
"wallet": 0,
        "wallet_limit": "10",
        "history": {
            "data": [
                {
                    "wallet_type": "minus",
                    "amount": 0
                }
            ],
            "links": {
                "first": "https://hassan.my-staff.net/api/v1/wallet?page=1",
                "last": "https://hassan.my-staff.net/api/v1/wallet?page=1",
                "p
 */
