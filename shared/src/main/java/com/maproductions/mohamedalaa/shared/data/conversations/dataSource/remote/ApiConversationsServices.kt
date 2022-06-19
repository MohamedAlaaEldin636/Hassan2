package com.maproductions.mohamedalaa.shared.data.conversations.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.presentation.conversations.ItemConversation
import com.maproductions.mohamedalaa.shared.presentation.conversations.ResponseChatDetails
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.*

interface ApiConversationsServices {

    @GET("chats")
    suspend fun getConversationsList(
        @Query(ApiConst.Query.PAGE) page: Int,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<MABasePaging<ItemConversation>>

    @GET("chat/messages")
    suspend fun getChatMessages(
        @Query(ApiConst.Query.USER_ID) otherAccountId: Int,
        @Query(ApiConst.Query.ORDER_ID) orderId: Int,
        @Query(ApiConst.Query.PAGE) page: Int,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<ResponseChatDetails>

    @Multipart
    @POST("message")
    suspend fun sendChatMessage(
        @Part(ApiConst.Query.USER_ID) otherAccountId: Int,
        @Part(ApiConst.Query.ORDER_ID) orderId: Int,
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part list: List<@JvmSuppressWildcards MultipartBody.Part>,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<Any>

}