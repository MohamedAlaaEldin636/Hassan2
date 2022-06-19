package com.maproductions.mohamedalaa.shared.data.shared.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.domain.home.ResponseHomeUser
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeCategory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSharedServices {

    @GET("search-text-result")
    suspend fun searchCategories(
        @Query(ApiConst.Query.TEXT) query: String,
        @Query(ApiConst.Query.PAGE) page: Int,
    ): MABaseResponse<MABasePaging<SliderHomeCategory>>

}