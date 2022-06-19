package com.maproductions.mohamedalaa.shared.domain.pusher

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.OrdersCategory
import com.maproductions.mohamedalaa.shared.core.extensions.fromJson
import com.maproductions.mohamedalaa.shared.core.extensions.fromJsonOrNull
import com.maproductions.mohamedalaa.shared.domain.auth.ProviderData
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeCategory
import com.maproductions.mohamedalaa.shared.domain.orders.ProviderInOrder
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress
import org.json.JSONObject

data class ResponsePusherOrder(
    var order: OrderInPusher?,
) {
    companion object {

        fun fromJson(data: String, gson: Gson): ResponsePusherOrder = fromJsonOrNull(data, gson)!!

        fun fromJsonOrNull(data: String, gson: Gson): ResponsePusherOrder? {
            val response = data.fromJsonOrNull<ResponsePusherOrder>(gson)

            val jsonObject = kotlin.runCatching { JSONObject(data) }.getOrNull()
            val orderJsonObject = jsonObject?.optJSONObject("order")
            val status = orderJsonObject?.opt("status")
            response?.order?.apiOrderStatus = (when (status) {
                is String -> status
                is JSONObject -> status.optString("status")
                else -> null
            })?.let { actualStatus ->
                ApiOrderStatus.values().firstOrNull { it.apiValue == actualStatus }
            }

            return response
        }

    }
}

data class ResponsePusherProvider(
    var id: Int?,
    var name: String?,
)

data class ResponsePusherUser(
    var id: Int?,
    var name: String?,
    var image: String?,
)

data class OrderInPusher(
    var id: Int?,
    //var status: String?,
    var category: SliderHomeCategory?,
    var provider: ResponsePusherProvider?,
    var user: ResponsePusherUser?,
    @SerializedName("ordered_at") var orderedAt: String?,
    var address: ResponseAddress?,
    var total: Float?,
    var services: List<ServiceInCategory>?,
    var apiOrderStatus: ApiOrderStatus?,
) {
    val statusOfOrder get() = apiOrderStatus//ApiOrderStatus.values().firstOrNull { it.apiValue == status/*?.orderStatus*/ }

}

data class StatusInPusher(
    @SerializedName("status") var orderStatus: String?,
) {
    val statusOfOrder get() = ApiOrderStatus.values().firstOrNull { it.apiValue == orderStatus }
}

/*
{
  "order": {
    "id": 90,
    "ordered_at_date": "الأربعاء - يونيو , 08/06/2022",
    "ordered_at_time": "9:45 ص",
    "total": 120,
    "delivery_cost": 0,
    "extra_notes": null,
    "user": {
      "id": 56,
      "name": "مممم مممممم",
      "phone": "123456123456",
      "image": "/tmp/phpGFanEv"
    },
    "status": "pending",
    "cancellation_fees": 0,
    "order_min_price_for_extra": 0,
    "services": [
      {
        "id": 1,
        "category_id": 1,
        "name": "الدهان",
        "count": 1,
        "price": 120,
        "additional": 0
      }
    ],
    "ordered_at": "الأربعاء - يونيو , 08/06/2022 9:45 ص"
  }
}
 */


/*
{
  "order": {
    "id": 23,
    "order_number": "#629479f911322",
    "order_type": "urgent",
    "promo_id": null,
    "extra_notes": null,
    "provider_total": 100,
    "app_total": 0,
    "delivery_cost": 0,
    "total": 100,
    "user_collected_money": 0,
    "collected_money_flag": 0,
    "ordered_at": "2022-05-17T08:22:00.000000Z",
    "address": {
      "id": 2,
      "title": "Home",
      "street_name": "هاشم الأشقر",
      "extra_description": "test",
      "latitude": 30.1259003,
      "longitude": 31.3728573,
      "address": "3 هاشم الأشقر، الهايكستب، قسم النزهة، محافظة القاهرة‬ 11511",
      "city_id": 1,
      "area_id": 1,
      "user_id": 8
    },
    "provider": {
      "id": 42,
      "account_type": "provider",
      "verified": 1,
      "approved": 1,
      "category_id": 1,
      "premium": 0,
      "company": 0,
      "name": "م",
      "email": null,
      "phone": "147",
      "latitude": 30.12592746223,
      "longitude": 31.375088654459,
      "address": "3 هاشم الأشقر، الهايكستب، قسم النزهة، محافظة القاهرة‬ 4473205، مصر",
      "wallet": 0,
      "points": 0,
      "birth_date": "2022-05-24",
      "relative_phone": "333",
      "average_rate": 0,
      "rate_count": 0,
      "link_unique_id": "#62936ac0eee1c",
      "referable_id": null,
      "social_id": null,
      "image": "/tmp/phpNeXhju",
      "orders_flag": 0,
      "notifications_count": 0,
      "created_at": "2022-05-24T07:09:21.000000Z",
      "updated_at": "2022-05-29T12:44:48.000000Z"
    },
    "user": {
      "id": 8,
      "account_type": "user",
      "verified": 1,
      "approved": 0,
      "category_id": null,
      "premium": 0,
      "company": 0,
      "name": "new 1",
      "email": "ajbujijoj@homail.com",
      "phone": "111",
      "latitude": 30.010805221782,
      "longitude": 31.176762767136,
      "address": "256G+8PJ، طريق كفر طهرمس، كفر طهرمس، بولاق الدكرور، الجيزة 3710501، مصر",
      "wallet": 0,
      "points": 0,
      "birth_date": null,
      "relative_phone": null,
      "average_rate": 0,
      "rate_count": 0,
      "link_unique_id": "#628f8f8e56c55",
      "referable_id": null,
      "social_id": null,
      "image": "/tmp/phpiQMcGs",
      "orders_flag": 0,
      "notifications_count": 0,
      "created_at": "2022-05-09T12:57:00.000000Z",
      "updated_at": "2022-05-26T14:32:46.000000Z"
    },
    "status": {
      "id": 190,
      "status": "arrived",
      "order_id": 23,
      "reason_id": null,
      "created_at": "2022-05-30T08:02:01.000000Z"
    },
    "images": [
      {
        "id": 41,
        "file": "https://hassan.my-staff.net/storage/orders/R2SAvuj8e8bLDDrP8R1lgyT8aPTaLyB8z67g3m7Z.jpg",
        "file_type": "image"
      }
    ]
  }
}
 */
