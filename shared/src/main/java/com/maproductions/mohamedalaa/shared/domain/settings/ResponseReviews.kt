package com.maproductions.mohamedalaa.shared.domain.settings

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging

data class ResponseReviews(
    @SerializedName("average_rate") var averageRate: Float?,
    @SerializedName("rate_count") var rateCount: Int?,
    var reviews: MABasePaging<ItemReview>?,
)
/*
"data": {
    "average_rate": 2.0571428571428,
    "rate_count": 7,
    "reviews": {
        "data": [
            {
                "rate": 0.4,
                "review": "هاااى",
                "user_name": "new 1",
                "created_at": "01-06-2022"
            },
            {
                "rate": 0.5,
                "review": "oooo",
                "user_name": "مممم",
                "created_at": "05-06-2022"
            },
            {
                "rate": 5,
                "review": "Very Good",
                "user_name": "مممم",
                "created_at": "05-06-2022"
            },
            {
                "rate": 0.5,
                "review": "tutitjrhr",
                "user_name": "مممم",
                "created_at": "05-06-2022"
            },
            {
                "rate": 0.5,
                "review": "tvfcfcrcrvfvfvfvtvr",
                "user_name": "مممم",
                "created_at": "05-06-2022"
            },
            {
                "rate": 5,
                "review": "vvvvv",
                "user_name": "مممم",
                "created_at": "05-06-2022"
            },
            {
                "rate": 2.5,
                "review": "inkn",
                "user_name": "مممم",
                "created_at": "05-06-2022"
            }
        ],
        "links": {
            "first": "https://hassan.my-staff.net/api/v1/provider/rates?page=1",
            "last": "https://hassan.my-staff.net/api/v1/provider/rates?page=1",
            "prev": null,
            "next": null
        },
        "meta": {
            "current_page": 1,
            "from": 1,
            "last_page": 1,
            "links": [
                {
                    "url": null,
                    "label": "pagination.previous",
                    "active": false
                },
                {
                    "url": "https://hassan.my-staff.net/api/v1/provider/rates?page=1",
                    "label": "1",
                    "active": true
                },
                {
                    "url": null,
                    "label": "pagination.next",
                    "active": false
                }
            ],
            "path": "https://hassan.my-staff.net/api/v1/provider/rates",
            "per_page": 20,
            "to": 7,
            "total": 7
        }
    }
}
 */
