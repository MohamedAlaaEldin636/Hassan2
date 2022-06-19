package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress

/**
 * @param orderedAt 2022-03-01 22:30:00
 *
 * @param provider `null` if user is trying to get pending orders isa.
 */
data class ResponseOrder(
    var id: Int,
    var category: String,
    @SerializedName("order_number") var orderNumber: String,
    @SerializedName("ordered_at") var orderedAt: String,
    var address: ResponseAddress?,
    var provider: ProviderInOrder?,
    var user: UserInOrder,
    @SerializedName("cancellation_fees") var cancellationFeesPercent: Float,
    var services: List<ServiceInOrdersList>,
    var total: Float,
)
/*
                "": "#621f723e50ac8",
                "ordered_at": "2022-03-01 22:30:00",
                "category": "Carpentry",
                "address": {
                    "title": "Home",
                    "street_name": "هاشم الأشقر",
                    "extra_description": "test",
                    "latitude": 30.1259003,
                    "longitude": 31.3728573,
                    "address": "3 هاشم الأشقر، الهايكستب، قسم النزهة، محافظة القاهرة\u202c 11511",
                    "city": "Cairo",
                    "area": "El Nozha"
                },
                "provider": {
                    "id": 1,
                    "name": "Eman",
                    "phone": "01205577043",
                    "image": "providers/profile/nPfbHGh77ms1gsnlWlFjU6YYHhWNptKADRWGmmT3.jpg",
                    "average_rate": 0
                },
                "user": {
                    "id": 2,
                    "name": "Eman",
                    "phone": "01205577043",
                    "image": null
                }
            }
 */
