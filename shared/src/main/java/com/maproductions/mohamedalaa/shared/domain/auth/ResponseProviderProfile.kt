package com.maproductions.mohamedalaa.shared.domain.auth

import com.google.gson.annotations.SerializedName

data class ResponseProviderProfile(
    var name: String?,
    @SerializedName("image") var imageUrl: String?,
    var phone: String?,
    /** "2022-06-19" */
    @SerializedName("birth_date") var birthDate: String?,
    var approved: Int?,
    var files: List<ProviderFile>?,
) {

    val isApproved get() = approved == 1

    val isSuspendedAccount get() = approved == 2

}

/*
{
    "code": 200,
    "message": "Successfully done",
    "data": {
        "id": 79,
        "account_type": "provider",
        "verified": 1,
        "approved": 1,
        "category_id": 1,
        "premium": 0,
        "company": 0,
        "name": "ياهي",
        "email": null,
        "phone": "022022",
        "latitude": 30.125912962804,
        "longitude": 31.375084631145,
        "address": "3 هاشم الأشقر، الهايكستب، قسم النزهة، محافظة القاهرة‬ 4473205، مصر",
        "wallet": 0,
        "points": 150,
        "birth_date": "2022-06-19",
        "relative_phone": "686864",
        "average_rate": 0,
        "rate_count": 0,
        "link_unique_id": "#62b03139c6edd",
        "referable_id": null,
        "social_id": null,
        "image": "/tmp/phpMvezt4",
        "orders_flag": 1,
        "orders_count_down": null,
        "notifications_count": 0,
        "created_at": "2022-06-19T09:50:05.000000Z",
        "updated_at": "2022-06-20T08:22:49.000000Z",
        "files": [
            {
                "id": 252,
                "file": "https://hassan.my-staff.net/storage//tmp/phpiypnxy",
                "file_type": "image",
                "mediable_type": "App\\Models\\User",
                "status": "approved",
                "type": "image_front_id"
            },
            {
                "id": 253,
                "file": "https://hassan.my-staff.net/storage//tmp/phpca4TG2",
                "file_type": "image",
                "mediable_type": "App\\Models\\User",
                "status": "approved",
                "type": "image_back_id"
            },
            {
                "id": 254,
                "file": "https://hassan.my-staff.net/storage/orders/syxYDgudNLH5xQbK9HkJpAFP6cIMw1ROvSB1izR3.jpg",
                "file_type": "image",
                "mediable_type": "App\\Models\\User",
                "status": "approved",
                "type": "image"
            }
        ],
        "media": [
            {
                "id": 252,
                "file": "https://hassan.my-staff.net/storage//tmp/phpiypnxy",
                "file_type": "image",
                "mediable_type": "App\\Models\\User",
                "status": "approved",
                "type": "image_front_id"
            },
            {
                "id": 253,
                "file": "https://hassan.my-staff.net/storage//tmp/phpca4TG2",
                "file_type": "image",
                "mediable_type": "App\\Models\\User",
                "status": "approved",
                "type": "image_back_id"
            },
            {
                "id": 254,
                "file": "https://hassan.my-staff.net/storage/orders/syxYDgudNLH5xQbK9HkJpAFP6cIMw1ROvSB1izR3.jpg",
                "file_type": "image",
                "mediable_type": "App\\Models\\User",
                "status": "approved",
                "type": "image"
            }
        ]
    }
}
 */
