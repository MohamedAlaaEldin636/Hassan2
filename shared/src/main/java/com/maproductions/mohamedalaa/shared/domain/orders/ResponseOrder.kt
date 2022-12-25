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
    var user: UserInOrder?,
    @SerializedName("cancellation_fees") var cancellationFeesPercent: Float,
    var services: List<ServiceInOrdersList>,
    var total: Float,
)

/*
"data": {
        "orders": {
            "data": [
                {
                    "id": 5,
                    "order_number": "#62af0b48aa9f4",
                    "total": 200,
                    "ordered_at": "Tuesday - March , 01/03/2022 10:30 PM",
                    "category": "Carpentry",
                    "address": {
                        "id": 1,
                        "title": "مممم",
                        "street_name": "تتت",
                        "extra_description": "ننن",
                        "latitude": 30.125905133113,
                        "longitude": 31.375092342496,
                        "address": "3 هاشم الأشقر، الهايكستب، قسم النزهة، محافظة القاهرة‬ 4473205، مصر",
                        "city": "Cairo",
                        "area": "EL Nozha"
                    },
                    "provider": null,
                    "services": [
                        {
                            "id": 1,
                            "category_id": 1,
                            "active": 1,
                            "price": 120,
                            "name": "Painting",
                            "pivot": {
                                "order_id": 5,
                                "service_id": 1,
                                "count": 2,
                                "price": 10,
                                "additional": 0
                            }
                        }
                    ],
                    "user": {
                        "id": 8,
                        "name": "new 1",
                        "phone": "111",
                        "image": "/tmp/phppToh27"
                    },
                    "cancellation_fees": 0
                }
            ],
            "links": {
                "first": "https://hassan.my-staff.net/api/v1/orders?page=1",
                "last": "https://hassan.my-staff.net/api/v1/orders?page=1",
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
                        "label": "&laquo; Previous",
                        "active": false
                    },
                    {
                        "url": "https://hassan.my-staff.net/api/v1/orders?page=1",
                        "label": "1",
                        "active": true
                    },
                    {
                        "url": null,
                        "label": "Next &raquo;",
                        "active": false
                    }
                ],
                "path": "https://hassan.my-staff.net/api/v1/orders",
                "per_page": 20,
                "to": 1,
                "total": 1
            }
        }
    }
 */
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
