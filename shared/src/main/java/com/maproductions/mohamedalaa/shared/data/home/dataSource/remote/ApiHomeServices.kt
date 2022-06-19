package com.maproductions.mohamedalaa.shared.data.home.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.extensions.convertToByteArray
import com.maproductions.mohamedalaa.shared.core.extensions.toJson
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.domain.home.ResponseCheckPromoCode
import com.maproductions.mohamedalaa.shared.domain.home.ResponseHomeUser
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeCategory
import com.maproductions.mohamedalaa.shared.domain.service.MM
import com.maproductions.mohamedalaa.shared.domain.service.RequestService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.*
import timber.log.Timber

interface ApiHomeServices {

    @GET("user/home")
    suspend fun getUserHome(): MABaseResponse<ResponseHomeUser>

    @GET("categories")
    suspend fun getCategories(): MABaseResponse<MABasePaging<SliderHomeCategory>>

    @GET("categories/{${ApiConst.Query.ID}}")
    suspend fun getServicesOfCategory(
        @Path(ApiConst.Query.ID) id: Int,
        @Query(ApiConst.Query.PAGE) page: Int,
    ): MABaseResponse<MABasePaging<ServiceInCategory>>

    @GET("check-promo-code")
    suspend fun checkPromoCode(
        @Query(ApiConst.Query.CODE) code: String,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<ResponseCheckPromoCode>

    @Multipart
    @POST("orders")
    suspend fun createOrder(
        @Part(ApiConst.Query.CATEGORY_ID) categoryId: Int,

        @Part(ApiConst.Query.ADDRESS_ID) addressId: Int,

        @Part(ApiConst.Query.ORDERED_AT) orderedAt: RequestBody,
        @Part(ApiConst.Query.ORDER_TYPE) orderType: RequestBody,

        @Part images: List<MultipartBody.Part>,
        @Part(ApiConst.Query.EXTRA_NOTES) extraNotes: RequestBody,

        @Part(ApiConst.Query.TOTAL) total: Float,

        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,

        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<Int> /* was returning Int which was order id but now reorder is from backend */

}