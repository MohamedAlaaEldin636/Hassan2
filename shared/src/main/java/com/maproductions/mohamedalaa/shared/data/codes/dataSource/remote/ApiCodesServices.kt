package com.maproductions.mohamedalaa.shared.data.codes.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.domain.codes.ResponseItemPromoCodes
import com.maproductions.mohamedalaa.shared.domain.codes.ResponseReplacePoints
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface ApiCodesServices {

    @GET("replace-points")
    suspend fun replacePoints(
        @HeaderMap headerMap: Map<String, String>
    ): MABaseResponse<ResponseReplacePoints>

    @GET("promo-codes")
    suspend fun getPromoCodes(
        @Query(ApiConst.Query.PAGE) page: Int,
        @HeaderMap headerMap: Map<String, String>
    ): MABaseResponse<MABasePaging<ResponseItemPromoCodes>>

}