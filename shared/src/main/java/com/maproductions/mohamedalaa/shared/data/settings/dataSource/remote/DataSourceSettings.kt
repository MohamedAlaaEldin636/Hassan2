package com.maproductions.mohamedalaa.shared.data.settings.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.di.module.OkHttpModule
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.remote.BaseRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Field
import retrofit2.http.Query
import javax.inject.Inject

class DataSourceSettings @Inject constructor(
    private val apiService: ApiSettingsServices,
) : BaseRemoteDataSource() {

    suspend fun getMyReviews(page: Int) = safeApiCall {
        apiService.getMyReviews(page, getAuthorizationHeader())
    }

    suspend fun getWalletDetails() = safeApiCall {
        apiService.getWalletDetails(getAuthorizationHeader())
    }

    suspend fun getWallet(page: Int) = safeApiCall {
        apiService.getWallet(page, getAuthorizationHeader())
    }

    suspend fun sendTrackingCurrentLocation(
        orderId: Int,
        latitude: String,
        longitude: String,
    ) = safeApiCall {
        apiService.sendTrackingCurrentLocation(orderId, latitude, longitude)
    }

    suspend fun getOnBoardScreenForUser() = safeApiCall {
        apiService.getOnBoardScreenForUser()
    }

    suspend fun getTermsAndConditionsForUser() = safeApiCall {
        apiService.getTermsAndConditionsForUser()
    }

    suspend fun getAboutAppForUser() = safeApiCall {
        apiService.getAboutAppForUser()
    }

    suspend fun getOnBoardScreenForProvider() = safeApiCall {
        apiService.getOnBoardScreenForProvider()
    }

    suspend fun getTermsAndConditionsForProvider() = safeApiCall {
        apiService.getTermsAndConditionsForProvider()
    }

    suspend fun getAboutAppForProvider() = safeApiCall {
        apiService.getAboutAppForProvider()
    }

    suspend fun contactUsOrSendComplainsOrSuggestions(
        name: String,
        email: String,
        phone: String,
        message: String,
        contactUsNotSendComplainsOrSuggestions: Boolean,
        orderId: Int?,
    ) = safeApiCall {
        apiService.contactUsOrSendComplainsOrSuggestions(
            name, email, phone, message,
            if (contactUsNotSendComplainsOrSuggestions) {
                ApiConst.Query.CONTACT_US
            }else {
                ApiConst.Query.COMPLAINT
            },
            if (orderId == null) emptyMap() else mapOf(ApiConst.Query.ORDER_ID to orderId.toString())
        )
    }

    suspend fun getPoints() = safeApiCall {
        apiService.getPoints(getAuthorizationHeader())
    }

    suspend fun getUserAddresses() = safeApiCall {
        apiService.getUserAddresses(getAuthorizationHeader())
    }

    suspend fun deleteUserAddress(id: Int) = safeApiCall {
        apiService.deleteUserAddress(id, getAuthorizationHeader())
    }

    suspend fun getCities() = safeApiCall {
        apiService.getCities()
    }

    suspend fun getAreas(cityId: Int) = safeApiCall {
        apiService.getAreas(cityId)
    }

    suspend fun addAddress(
        title: String,
        streetName: String,
        extraDescription: String,
        latitude: String,
        longitude: String,
        address: String,
        cityId: Int?,
        areaId: Int?,
    ) = safeApiCall {
        val map = mutableMapOf<String, String>()
        if (extraDescription.isNotEmpty()) {
            map[ApiConst.Query.EXTRA_DESCRIPTION] = extraDescription
        }

        if (cityId != null) {
            map[ApiConst.Query.CITY_ID] = cityId.toString()
        }

        if (streetName.isNotEmpty()) {
            map[ApiConst.Query.STREET_NAME] = streetName
        }

        if (areaId != null) {
            map[ApiConst.Query.AREA_ID] = areaId.toString()
        }

        apiService.addAddress(
            title,
            latitude, longitude, address,
            map,
            getAuthorizationHeader()
        )
    }

    suspend fun getNotificationsCount() = safeApiCall {
        apiService.getNotificationsCount(getAuthorizationHeader())
    }

    suspend fun getNotifications(page: Int) = safeApiCall {
        apiService.getNotifications(
            page,
            listOfNotNull(prefsAccount.getApiBearerToken().firstOrNull()).associateBy {
                OkHttpModule.HEADER_KEY_AUTHORIZATION
            }
        )
    }

}
