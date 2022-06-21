@file:OptIn(ExperimentalCoroutinesApi::class)

package com.maproductions.mohamedalaa.shared.data.local.preferences

import android.content.Context
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationData
import com.maproductions.mohamedalaa.shared.domain.auth.ProviderData
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeCategory
import com.maproductions.mohamedalaa.shared.domain.local.preferences.UserData
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.pusher.pushnotifications.PushNotifications
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * - Shared between Prefs User & Prefs Provider
 */
@Singleton
class PrefsAccount @Inject constructor(
    @ApplicationContext context: Context,
    gson: Gson,
    private val prefsSplash: PrefsSplash,
) : PrefsBase(context, gson, "PREFS_ACCOUNT") {

    companion object {
        private const val KEY_API_BEARER_TOKEN = "KEY_API_BEARER_TOKEN"
        private const val KEY_USER_DATA = "KEY_USER_DATA"
        private const val KEY_PROVIDER_DATA = "KEY_PROVIDER_DATA"
        private const val KEY_NOTIFICATIONS_COUNT = "KEY_NOTIFICATIONS_COUNT"
        private const val KEY_SEARCH_CATEGORIES_SUGGESTIONS = "KEY_SEARCH_CATEGORIES_SUGGESTIONS"
        private const val KEY_SEARCH_USER_ORDERS_SUGGESTIONS = "KEY_SEARCH_USER_ORDERS_SUGGESTIONS"
        private const val KEY_SEARCH_PROVIDER_ORDERS_SUGGESTIONS = "KEY_SEARCH_PROVIDER_ORDERS_SUGGESTIONS"
    }

    fun getIdOfAccount(isUserNotProvider: Boolean) = if (isUserNotProvider) {
        getUserData().map { it?.id }
    }else {
        getProviderData().map { it?.id }
    }

    suspend fun setApiBearerToken(token: String) =
        setStringValue(KEY_API_BEARER_TOKEN, token)

    fun getApiBearerToken() = getStringValue(KEY_API_BEARER_TOKEN)

    suspend fun setUserData(userData: UserData) = setValue(KEY_USER_DATA, userData)
    suspend fun updateUserDataLocationData(locationData: LocationData) {
        val userData = getUserData().firstOrNull() ?: UserData()

        setUserData(
            userData.copy(
                latitude = locationData.latitude,
                longitude = locationData.longitude,
                address = locationData.address,
            )
        )
    }
    fun getUserData() = getValue<UserData>(KEY_USER_DATA)

    suspend fun setProviderData(providerData: ProviderData) = setValue(KEY_PROVIDER_DATA, providerData)
    fun getProviderData() = getValue<ProviderData>(KEY_PROVIDER_DATA)

    fun getUserLocationData(): Flow<LocationData?> = getUserData().mapLatest { userData ->
        userData?.let {
            LocationData(it.latitude, it.longitude, it.address)
        }
    }

    suspend fun setNotificationsCount(count: Int) = setIntValue(KEY_NOTIFICATIONS_COUNT, count)
    fun getNotificationsCount() = getIntValue(KEY_NOTIFICATIONS_COUNT)

    suspend fun addSearchCategoriesSuggestions(category: SliderHomeCategory) {
        val set = getSearchCategoriesSuggestions().firstOrNull().orEmpty().takeLast(2).toMutableSet()
        set += category

        setValue(KEY_SEARCH_CATEGORIES_SUGGESTIONS, set.toList())
    }
    fun getSearchCategoriesSuggestions() = getValue<List<SliderHomeCategory>>(KEY_SEARCH_CATEGORIES_SUGGESTIONS)

    suspend fun addSearchUserOrdersSuggestions(category: ResponseOrder) {
        val set = getSearchUserOrdersSuggestions().firstOrNull().orEmpty().takeLast(2).toMutableSet()
        set += category

        setValue(KEY_SEARCH_USER_ORDERS_SUGGESTIONS, set.toList())
    }
    fun getSearchUserOrdersSuggestions() = getValue<List<ResponseOrder>>(KEY_SEARCH_USER_ORDERS_SUGGESTIONS)

    suspend fun addSearchProviderOrdersSuggestions(category: ResponseOrder) {
        val set = getSearchProviderOrdersSuggestions().firstOrNull().orEmpty().takeLast(2).toMutableSet()
        set += category

        setValue(KEY_SEARCH_PROVIDER_ORDERS_SUGGESTIONS, set.toList())
    }
    fun getSearchProviderOrdersSuggestions() = getValue<List<ResponseOrder>>(KEY_SEARCH_PROVIDER_ORDERS_SUGGESTIONS)

    suspend fun logOut() {
        prefsSplash.setInitialLaunch(SplashInitialLaunch.LOGIN)
        PushNotifications.clearAllState()
        setApiBearerToken("")
        setUserData(UserData())
        setProviderData(ProviderData())
        setNotificationsCount(0)
    }

}