package com.maproductions.mohamedalaa.shared.data.myAccount.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.presentation.myAccount.ResponsePreviousWorkData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiMyAccountServices {

    @GET("provider/previous-work")
    suspend fun getPreviousWorkData(
        @HeaderMap headerMap: Map<String, String>
    ): MABaseResponse<ResponsePreviousWorkData>

    @Multipart
    @POST("provider/previous-work")
    suspend fun addOrUpdatePreviousWorkData(
        @Part images: List<@JvmSuppressWildcards MultipartBody.Part>,
        @Part video: MultipartBody.Part,
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @HeaderMap headerMap: Map<String, String>
    ): MABaseResponse<Any>
    @Multipart
    @POST("provider/previous-work")
    suspend fun addOrUpdatePreviousWorkData(
        @Part video: MultipartBody.Part,
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @HeaderMap headerMap: Map<String, String>
    ): MABaseResponse<Any>
    @Multipart
    @POST("provider/previous-work")
    suspend fun addOrUpdatePreviousWorkData(
        @Part images: List<@JvmSuppressWildcards MultipartBody.Part>,
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @HeaderMap headerMap: Map<String, String>
    ): MABaseResponse<Any>
    @Multipart
    @POST("provider/previous-work")
    suspend fun addOrUpdatePreviousWorkData(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @HeaderMap headerMap: Map<String, String>
    ): MABaseResponse<Any>

    @FormUrlEncoded
    @POST("provider/previous-work/{${ApiConst.Query.ID}}")
    suspend fun deleteImageOrVideoOfPreviousWorkData(
        @Path(ApiConst.Query.ID) id: Int,
        @HeaderMap headerMap: Map<String, String>,
        @Field(ApiConst.Query.METHOD) method: String = ApiConst.Value.DELETE,
    ): MABaseResponse<Any>

}
