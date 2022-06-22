package com.maproductions.mohamedalaa.shared.data.home.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.BasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.customTypes.MAResult
import com.maproductions.mohamedalaa.shared.core.di.module.GsonModule
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.remote.BaseRemoteDataSource
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.domain.service.MM
import com.maproductions.mohamedalaa.shared.domain.service.RequestService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.http.Part
import timber.log.Timber
import javax.inject.Inject

class DataSourceHome @Inject constructor(
    private val apiService: ApiHomeServices,
) : BaseRemoteDataSource() {

    suspend fun getUserHome() = safeApiCall {
        apiService.getUserHome()
    }

    suspend fun getCategories() = safeApiCall {
        apiService.getCategories()
    }

    suspend fun getServicesOfCategory(id: Int, page: Int) = safeApiCall {
        apiService.getServicesOfCategory(id, page)
    }

    suspend fun checkPromoCode(code: String) = safeApiCall {
        apiService.checkPromoCode(code, getAuthorizationHeader())
    }

    suspend fun createOrder(
        categoryId: Int,

        services: List<ServiceInCategoryWithCount>,

        addressId: Int,

        orderedAt: String,
        orderType: String,

        images: List<MultipartBody.Part>,
        extraNotes: String,

        total: Float,

        promoId: Int?,
    ): MAResult.Immediate<MABaseResponse<Int>> = safeApiCall {
        val map = mutableMapOf<String, RequestBody>()
        for ((index, item) in services.withIndex()) {
            map["${ApiConst.Query.SERVICES}[$index][${ApiConst.Query.SERVICE_ID}]"] = item.serviceInCategory.id.toString().toRequestBody()
            map["${ApiConst.Query.SERVICES}[$index][${ApiConst.Query.PRICE}]"] = item.serviceInCategory.price.toString().toRequestBody()
            map["${ApiConst.Query.SERVICES}[$index][${ApiConst.Query.COUNT}]"] = item.count.toString().toRequestBody()
        }

        Timber.e("promo id discount.value?.id $promoId")
        if (promoId != null) {
            map[ApiConst.Query.PROMO_ID] = promoId.toString().toRequestBody()
        }

        apiService.createOrder(
            categoryId,

            addressId,

            orderedAt.toRequestBody(),
            orderType.toRequestBody(),

            images,
            extraNotes.toRequestBody(),

            total,

            map,

            getAuthorizationHeader(),
        )
    }

}
