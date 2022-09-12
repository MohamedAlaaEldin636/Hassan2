package com.maproductions.mohamedalaa.shared.core.customTypes

import com.maproductions.mohamedalaa.shared.core.di.module.RetrofitModule
import com.maproductions.mohamedalaa.shared.presentation.main.SharedMainActivity
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.pushnotifications.BeamsCallback
import com.pusher.pushnotifications.PushNotifications
import com.pusher.pushnotifications.PusherCallbackError
import com.pusher.pushnotifications.auth.AuthData
import com.pusher.pushnotifications.auth.AuthDataGetter
import com.pusher.pushnotifications.auth.BeamsTokenProvider
import timber.log.Timber

object StaticValues {

    var isUserNotProvider = false

    var mainActivityClazz: Class<*> = SharedMainActivity::class.java

}

object PusherUtils {
    const val BEAMS_INSTANCE_ID = "c5a46e59-98a1-496e-a9f1-995e061cf08b"

    const val CHANNELS_CLUSTER = "eu"

    const val CHANNELS_APP_KEY = "e03a9f006f0981db304d"

    private const val EVENT_NAME_PREFIX = "App\\Events\\"

    private const val BEAMS_TOKEN_URL_FOR_USERS = "${RetrofitModule.BASE_URL}user/generate-beams-token"
    private const val BEAMS_TOKEN_URL_FOR_PROVIDERS = "${RetrofitModule.BASE_URL}provider/generate-beams-token"

    fun getChannelNameTrackProviderLocation(orderId: Int) = "order-$orderId"
    const val EVENT_NAME_TRACK_PROVIDER_LOCATION = "${EVENT_NAME_PREFIX}SendCurrentLocation"

    fun getChannelNameOnChangeOrderStatusForOrder(orderId: Int) = "order-$orderId"
    const val EVENT_NAME_ON_CHANGE_ORDER_STATUS_FOR_ORDER = "${EVENT_NAME_PREFIX}ToggleOrderEvent"

    fun getChannelNameOnChangeOrderStatusForAccount(accountId: Int) = "my-orders-$accountId"
    const val EVENT_NAME_ON_CHANGE_ORDER_STATUS_FOR_ACCOUNT = "${EVENT_NAME_PREFIX}MyOrdersEvent"

    fun getChannelNameChatsForAccount(accountId: Int) = "my-message-$accountId"
    const val EVENT_NAME_CHATS_FOR_ACCOUNT = "${EVENT_NAME_PREFIX}MyMessagesEvent"

    fun getChannelNameChatMessagesForOrder(accountId: Int) = "message-$accountId"
    const val EVENT_NAME_CHAT_MESSAGES_FOR_ORDER = "${EVENT_NAME_PREFIX}SendMessageEvent"

    fun getChannelNameWallet(accountId: Int) = "my-wallet-$accountId"
    const val EVENT_NAME_WALLET = "${EVENT_NAME_PREFIX}MyWalletEvent"

    fun getChannelNewOrder(accountId: Int) = "order-$accountId"
    const val EVENT_NAME_NEW_ORDER = "${EVENT_NAME_PREFIX}SendOrderEvent"
    /*
    {
  "order": {
    "id": 69,
    "order_number": "#629e0546776e1",
    "order_type": "urgent",
    "promo_id": null,
    "extra_notes": null,
    "provider_total": 320,
    "app_total": 0,
    "delivery_cost": 0,
    "total": 320,
    "user_collected_money": 0,
    "collected_money_flag": 0,
    "ordered_at": "2022-06-06T13:46:00.000000Z",
    "user": {
      "id": 56,
      "account_type": "user",
      "verified": 1,
      "approved": 1,
      "category_id": null,
      "premium": 0,
      "company": 0,
      "name": "مممم",
      "email": null,
      "phone": "123456123456",
      "latitude": 30.125947471434,
      "longitude": 31.375093013048,
      "address": "3 هاشم الأشقر، الهايكستب، قسم النزهة، محافظة القاهرة‬ 4473205، مصر",
      "wallet": 3823,
      "points": 700,
      "birth_date": null,
      "relative_phone": null,
      "average_rate": 0,
      "rate_count": 0,
      "link_unique_id": "#629dff62b0244",
      "referable_id": null,
      "social_id": null,
      "image": null,
      "orders_flag": 0,
      "notifications_count": 0,
      "created_at": "2022-06-02T13:48:30.000000Z",
      "updated_at": "2022-06-06T13:21:38.000000Z"
    },
    "services": [
      {
        "id": 1,
        "category_id": 1,
        "active": 1,
        "price": 120,
        "name": "الدهان"
      },
      {
        "id": 2,
        "category_id": 1,
        "active": 1,
        "price": 100,
        "name": "تصليح"
      }
    ]
  }
}
     */

    fun subscribeToEventInChannel(
        channelName: String,
        eventName: String,
        onGettingData: (data: String) -> Unit
    ) {
        val options = PusherOptions()
        options.setCluster(CHANNELS_CLUSTER)

        val pusher = Pusher(CHANNELS_APP_KEY, options)
        pusher.connect()

        runCatching {
            pusher.unsubscribe(channelName)
        }

        val channel = pusher.subscribe(channelName)

        channel.bind(eventName) { event ->
            onGettingData(event.data)
        }
    }

    fun loginBeams(isUserNotProvider: Boolean, apiToken: String, id: Int) {
        Timber.e("BeamsCallback -> Pre login $apiToken $isUserNotProvider $id")

        val tokenProvider = BeamsTokenProvider(
            if (isUserNotProvider) BEAMS_TOKEN_URL_FOR_USERS else BEAMS_TOKEN_URL_FOR_PROVIDERS,
            object : AuthDataGetter {
                override fun getAuthData(): AuthData {
                    val headerMap = mapOf(
                        "Authorization" to "Bearer $apiToken"
                    )

                    return AuthData(
                        headerMap,
                        emptyMap()
                    )
                }
            }
        )

        val beamNamePrefix = if (isUserNotProvider) "users" else "providers"
        Timber.e("BeamsCallback PRE 1 MORE -> ${beamNamePrefix}-${id}")

        PushNotifications.clearAllState()
        PushNotifications.setUserId(
            "${beamNamePrefix}-${id}",
            tokenProvider,
            object : BeamsCallback<Void, PusherCallbackError> {
                override fun onFailure(error: PusherCallbackError) {
                    // todo keep calling or don't login isa.
                    Timber.e("BeamsCallback -> Could not login to Beams: ${error.message}")
                }

                override fun onSuccess(vararg values: Void) {
                    Timber.e("BeamsCallback -> Beams login success")
                }
            }
        )

        Timber.e("BeamsCallback DONE")
    }

    fun getChannelEvent(
        channelName: String,
        eventName: String,
        onGettingData: (data: String) -> Unit
    ): ChannelEvent {
        val options = PusherOptions()
        options.setCluster(CHANNELS_CLUSTER)

        val pusher = Pusher(CHANNELS_APP_KEY, options)
        pusher.connect()

        return ChannelEvent(pusher, channelName, eventName, onGettingData)
    }/*{
        val options = PusherOptions()
        options.setCluster(CHANNELS_CLUSTER)

        val pusher = Pusher(CHANNELS_APP_KEY, options)
        pusher.connect()

        runCatching {
            pusher.unsubscribe(channelName)
        }

        val channel = pusher.subscribe(channelName)

        channel.bind(eventName) { event ->
            onGettingData(event.data)
        }
    }*/

    data class ChannelEvent(
        private val pusher: Pusher,
        private val channelName: String,
        private val eventName: String,
        private val onGettingData: (data: String) -> Unit
    ) {

        fun subscribe() {
            kotlin.runCatching {
                pusher.unsubscribe(channelName)
            }

            val channel = pusher.subscribe(channelName)

            channel.bind(eventName) { event ->
                onGettingData(event.data)
            }
        }

        fun unsubscribe() = pusher.unsubscribe(channelName)

    }

}


