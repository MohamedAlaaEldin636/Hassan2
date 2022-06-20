package com.maproductions.mohamedalaa.shared.data.orders.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.*
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.remote.BaseRemoteDataSource
import com.maproductions.mohamedalaa.shared.domain.home.RequestServiceWithCount
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseForUserOrders
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseListOfOrders
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class DataSourceOrder @Inject constructor(
    private val apiService: ApiOrderServices,
) : BaseRemoteDataSource() {

    suspend fun getAdditionalServices(orderId: Int) = safeApiCall {
        apiService.getAdditionalServices(orderId, getAuthorizationHeader())
    }

    suspend fun getCancellationReasons() = safeApiCall {
        apiService.getCancellationReasons()
    }

    suspend fun searchOrdersForProvider(
        text: String?,
        categoryId: Int?,
        cityId: Int?,
        page: Int,
    ): MAResult.Immediate<MABaseResponse<ResponseListOfOrders>> = safeApiCall {
        val map = mutableMapOf<String, String>()
        if (!text.isNullOrEmpty()) map[ApiConst.Query.TEXT] = text
        if (categoryId != null) map[ApiConst.Query.CATEGORY_ID] = categoryId.toString()
        if (cityId != null) map[ApiConst.Query.CITY_ID] = cityId.toString()

        apiService.searchOrdersForProvider(
            page,
            map,
            getAuthorizationHeader()
        )
    }

    suspend fun searchOrdersForUser(
        text: String?,
        categoryId: Int?,
        cityId: Int?,
        page: Int,
    ): MAResult.Immediate<MABaseResponse<ResponseForUserOrders>> = safeApiCall {
        val map = mutableMapOf<String, String>()
        if (!text.isNullOrEmpty()) map[ApiConst.Query.TEXT] = text
        if (categoryId != null) map[ApiConst.Query.CATEGORY_ID] = categoryId.toString()
        if (cityId != null) map[ApiConst.Query.CITY_ID] = cityId.toString()

        apiService.searchOrdersForUser(
            page,
            map,
            getAuthorizationHeader()
        )
    }

    suspend fun getOrdersForProvider(
        status: OrdersCategory,
        text: String?,
        categoryId: Int?,
        cityId: Int?,
        page: Int,
    ): MAResult.Immediate<MABaseResponse<ResponseListOfOrders>> = safeApiCall {
        val map = mutableMapOf<String, String>()
        if (!text.isNullOrEmpty()) map[ApiConst.Query.TEXT] = text
        if (categoryId != null) map[ApiConst.Query.CATEGORY_ID] = categoryId.toString()
        if (cityId != null) map[ApiConst.Query.CITY_ID] = cityId.toString()

        apiService.getOrdersForProvider(
            status.apiValue,
            page,
            map,
            getAuthorizationHeader()
        )
    }

    suspend fun getOrdersForUser(
        status: OrdersCategory,
        text: String?,
        categoryId: Int?,
        cityId: Int?,
        page: Int,
    ): MAResult.Immediate<MABaseResponse<ResponseForUserOrders>> = safeApiCall {
        val map = mutableMapOf<String, String>()
        if (!text.isNullOrEmpty()) map[ApiConst.Query.TEXT] = text
        if (categoryId != null) map[ApiConst.Query.CATEGORY_ID] = categoryId.toString()
        if (cityId != null) map[ApiConst.Query.CITY_ID] = cityId.toString()

        apiService.getOrdersForUser(
            status.apiValue,
            page,
            map,
            getAuthorizationHeader()
        )
    }

    suspend fun updateStatusForOrder(
        id: Int,
        status: ApiOrderStatus,

        collectedMoney: Float? = null,
        additionalServices: List<RequestServiceWithCount>? = null,
    ) = safeApiCall {
        val map = mutableMapOf<String, RequestBody>()
        if (collectedMoney != null) {
            map[ApiConst.Query.COLLECTED_MONEY] = collectedMoney.toString().toRequestBody()
        }
        if (!additionalServices.isNullOrEmpty()) {
            for ((index, item) in additionalServices.withIndex()) {
                map["${ApiConst.Query.ADDITIONAL_SERVICES}[$index][${ApiConst.Query.SERVICE_ID}]"] = item.serviceId.toString().toRequestBody()
                map["${ApiConst.Query.ADDITIONAL_SERVICES}[$index][${ApiConst.Query.PRICE}]"] = item.price.toString().toRequestBody()
                map["${ApiConst.Query.ADDITIONAL_SERVICES}[$index][${ApiConst.Query.COUNT}]"] = item.count.toString().toRequestBody()
            }
        }

        apiService.updateStatusForOrder(id, status.apiValue.toRequestBody(), map, getAuthorizationHeader())
    }

    suspend fun finishOrderForUser(
        id: Int,
        userCollectedMoney: Float?,
        collectedMoneyFlag: Int,
    ) = safeApiCall {
        val map = mutableMapOf<String, RequestBody>()
        if (userCollectedMoney != null) {
            map[ApiConst.Query.USER_COLLECTED_MONEY] = userCollectedMoney.toString().toRequestBody()
        }

        apiService.finishOrderForUser(id, collectedMoneyFlag, map, getAuthorizationHeader())
    }

    suspend fun cancelOrderWithReason(
        id: Int,
        orderId: Int,
    ) = safeApiCall {
        apiService.cancelOrderWithReason(id, orderId, getAuthorizationHeader())
    }

    /*
    suspend fun finishOrderForUser(
        @Path(ApiConst.Query.ID) id: Int,
        @Part(ApiConst.Query.COLLECTED_MONEY_FLAG) collectedMoneyFlag: Int,
        @PartMap queryMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @HeaderMap headerMap: Map<String, String>,
        @Part(ApiConst.Query.STATUS) status: RequestBody = ApiOrderStatus.FINISHED.apiValue.toRequestBody(),
        @Part(ApiConst.Query.METHOD) method: RequestBody = ApiConst.Value.PATCH.toRequestBody()
    )
     */

    suspend fun getOrderDetails(id: Int) = safeApiCall {
        apiService.getOrderDetails(id, getAuthorizationHeader())
    }

    suspend fun rateProvider(providerId: Int, rate: Float, review: String?) = safeApiCall {
        val paramsMap = mutableMapOf<String, String>()
        if (!review.isNullOrEmpty()) paramsMap[ApiConst.Query.REVIEW] = review
        apiService.rateProvider(
            providerId,
            rate,
            paramsMap,
            getAuthorizationHeader()
        )
    }

}