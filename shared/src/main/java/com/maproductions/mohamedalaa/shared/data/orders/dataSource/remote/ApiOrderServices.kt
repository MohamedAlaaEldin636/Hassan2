package com.maproductions.mohamedalaa.shared.data.orders.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.domain.home.ResponseHomeUser
import com.maproductions.mohamedalaa.shared.domain.orders.ItemCancellationReason
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseAdditionalServices
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrderDetails
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiOrderServices {

    @GET("cancellation-reasons")
    suspend fun getCancellationReasons(
        @Query(ApiConst.Query.APP_TYPE) appType: String = ApiConst.Query.PROVIDER,
    ): MABaseResponse<List<ItemCancellationReason>>

    @GET("additional-services")
    suspend fun getAdditionalServices(
        @Query(ApiConst.Query.ORDER_ID) status: Int,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<ResponseAdditionalServices>

    @GET("orders")
    suspend fun getOrders(
        @Query(ApiConst.Query.STATUS) status: String,
        @Query(ApiConst.Query.PAGE) page: Int,
        @QueryMap queryMap: Map<String, String>,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<MABasePaging<ResponseOrder>>

    @GET("orders")
    suspend fun searchOrders(
        @Query(ApiConst.Query.PAGE) page: Int,
        @QueryMap queryMap: Map<String, String>,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<MABasePaging<ResponseOrder>>

    @Multipart
    @POST("orders/{${ApiConst.Query.ID}}")
    suspend fun updateStatusForOrder(
        @Path(ApiConst.Query.ID) id: Int,
        @Part(ApiConst.Query.STATUS) status: RequestBody,
        @PartMap queryMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @HeaderMap headerMap: Map<String, String>,
        @Part(ApiConst.Query.METHOD) method: RequestBody = ApiConst.Value.PATCH.toRequestBody()
    ): MABaseResponse<Any>

    @Multipart
    @POST("orders/{${ApiConst.Query.ID}}")
    suspend fun finishOrderForUser(
        @Path(ApiConst.Query.ID) id: Int,
        @Part(ApiConst.Query.COLLECTED_MONEY_FLAG) collectedMoneyFlag: Int,
        @PartMap queryMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @HeaderMap headerMap: Map<String, String>,
        @Part(ApiConst.Query.STATUS) status: RequestBody = ApiOrderStatus.FINISHED.apiValue.toRequestBody(),
        @Part(ApiConst.Query.METHOD) method: RequestBody = ApiConst.Value.PATCH.toRequestBody()
    ): MABaseResponse<Any>

    @Multipart
    @POST("orders/{${ApiConst.Query.ID}}")
    suspend fun cancelOrderWithReason(
        @Path(ApiConst.Query.ID) id: Int,
        @Part(ApiConst.Query.REASON_ID) orderId: Int,
        @HeaderMap headerMap: Map<String, String>,
        @Part(ApiConst.Query.STATUS) status: RequestBody = ApiOrderStatus.CANCELLED.apiValue.toRequestBody(),
        @Part(ApiConst.Query.METHOD) method: RequestBody = ApiConst.Value.PATCH.toRequestBody()
    ): MABaseResponse<Any>

    @GET("orders/{${ApiConst.Query.ID}}")
    suspend fun getOrderDetails(
        @Path(ApiConst.Query.ID) id: Int,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<ResponseOrderDetails>

    @FormUrlEncoded
    @POST("user/add-rate")
    suspend fun rateProvider(
        @Field(ApiConst.Query.PROVIDER_ID) providerId: Int,
        @Field(ApiConst.Query.RATE) rate: Float,
        @FieldMap paramsMap: Map<String, String>,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<Any>

}