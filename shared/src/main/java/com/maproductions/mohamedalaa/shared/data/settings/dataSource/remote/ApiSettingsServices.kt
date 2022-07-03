package com.maproductions.mohamedalaa.shared.data.settings.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.domain.notifications.ItemNotification
import com.maproductions.mohamedalaa.shared.domain.settings.ItemOnBoard
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress
import com.maproductions.mohamedalaa.shared.domain.settings.ResponsePoints
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseReviews
import com.maproductions.mohamedalaa.shared.domain.wallet.ResponseWallet
import retrofit2.http.*

interface ApiSettingsServices {

    @GET("provider/rates")
    suspend fun getMyReviews(
        @Query(ApiConst.Query.PAGE) page: Int,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<ResponseReviews>

    @GET("provider/send-current-location")
    suspend fun sendTrackingCurrentLocation(
        @Query(ApiConst.Query.ORDER_ID) orderId: Int,
        @Query(ApiConst.Query.LATITUDE) latitude: String,
        @Query(ApiConst.Query.LONGITUDE) longitude: String,
    ): MABaseResponse<Any>

    @GET("settings?type=welcome&app_type=user")
    suspend fun getOnBoardScreenForUser(): MABaseResponse<List<ItemOnBoard>>

    @GET("settings?type=terms&app_type=user")
    suspend fun getTermsAndConditionsForUser(): MABaseResponse<ItemOnBoard>

    @GET("settings?type=about&app_type=user")
    suspend fun getAboutAppForUser(): MABaseResponse<ItemOnBoard>

    @GET("settings?type=welcome&app_type=provider")
    suspend fun getOnBoardScreenForProvider(): MABaseResponse<List<ItemOnBoard>>

    @GET("settings?type=terms&app_type=provider")
    suspend fun getTermsAndConditionsForProvider(): MABaseResponse<ItemOnBoard>

    @GET("settings?type=about&app_type=provider")
    suspend fun getAboutAppForProvider(): MABaseResponse<ItemOnBoard>

    @FormUrlEncoded
    @POST("contact-app")
    suspend fun contactUsOrSendComplainsOrSuggestions(
        @Field(ApiConst.Query.NAME) name: String,
        @Field(ApiConst.Query.EMAIL) email: String,
        @Field(ApiConst.Query.PHONE) phone: String,
        @Field(ApiConst.Query.MESSAGE) message: String,
        @Field(ApiConst.Query.TYPE) type: String,
        @FieldMap paramsMap: Map<String, String>,
    ): MABaseResponse<Any>

    @GET("points")
    suspend fun getPoints(
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<ResponsePoints>

    @GET("user/address")
    suspend fun getUserAddresses(
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<List<ResponseAddress>>

    @FormUrlEncoded
    @POST("user/address/{${ApiConst.Query.ID}}")
    suspend fun deleteUserAddress(
        @Path(ApiConst.Query.ID) id: Int,
        @HeaderMap headerMap: Map<String, String>,
        @Field(ApiConst.Query.METHOD) method: String = ApiConst.Value.DELETE,
    ): MABaseResponse<Any>

    @GET("cities")
    suspend fun getCities(): MABaseResponse<List<IdAndName>>

    @GET("areas")
    suspend fun getAreas(
        @Query(ApiConst.Query.CITY_ID) cityId: Int,
    ): MABaseResponse<List<IdAndName>>

    @FormUrlEncoded
    @POST("user/address")
    suspend fun addAddress(
        @Field(ApiConst.Query.TITLE) title: String,
        //@Field(ApiConst.Query.EXTRA_DESCRIPTION) extraDescription: String,
        @Field(ApiConst.Query.LATITUDE) latitude: String,
        @Field(ApiConst.Query.LONGITUDE) longitude: String,
        @Field(ApiConst.Query.ADDRESS) address: String,
        @FieldMap map: Map<String, String>,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<Any>

    @GET("notifications/count")
    suspend fun getNotificationsCount(
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<Int>

    @GET("notifications")
    suspend fun getNotifications(
        @Query(ApiConst.Query.PAGE) page: Int,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<MABasePaging<ItemNotification>>

    @GET("wallet")
    suspend fun getWalletDetails(
        @HeaderMap headerMap: Map<String, String>,
        @Query(ApiConst.Query.PAGE) page: Int = 1,
    ): MABaseResponse<ResponseWallet>

    @GET("wallet")
    suspend fun getWallet(
        @Query(ApiConst.Query.PAGE) page: Int,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<ResponseWallet>

}