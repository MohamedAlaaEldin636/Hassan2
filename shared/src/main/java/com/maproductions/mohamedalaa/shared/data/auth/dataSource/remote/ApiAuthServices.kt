package com.maproductions.mohamedalaa.shared.data.auth.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.domain.auth.ProviderData
import com.maproductions.mohamedalaa.shared.domain.auth.ResponseUpdateProfile
import com.maproductions.mohamedalaa.shared.domain.auth.ResponseVerifyCode
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiAuthServices {

    @FormUrlEncoded
    @POST("provider/update-service")
    suspend fun updateCategoryAndServices(
        @Field(ApiConst.Query.CATEGORY_ID) categoryId: Int,
        @Field(ApiConst.Query.SERVICES + "[]") servicesIds: List<Int>,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<Any>

    @FormUrlEncoded
    @POST("provider/auth/forget-password")
    suspend fun forgetPassword(
        @Field(ApiConst.Query.PHONE) phone: String,
        @Field(ApiConst.Query.PASSWORD) password: String,
        @Field(ApiConst.Query.PASSWORD_CONFIRMATION) confirmationPassword: String,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<Any>

    @FormUrlEncoded
    @POST("auth/send-code")
    suspend fun resendCode(
        @Field(ApiConst.Query.PHONE) phone: String,
        @Field(ApiConst.Query.TYPE) type: String,
        @Field(ApiConst.Query.ACCOUNT_TYPE) accountType: String,
    ): MABaseResponse<Any>

    /** @return user bearer token */
    @FormUrlEncoded
    @POST("auth/verify")
    suspend fun verifyCode(
        @Field(ApiConst.Query.PHONE) phone: String,
        @Field(ApiConst.Query.CODE) code: String,
        @Field(ApiConst.Query.TYPE) type: String,
        @Field(ApiConst.Query.ACCOUNT_TYPE) accountType: String,
    ): MABaseResponse<ResponseVerifyCode>

    @FormUrlEncoded
    @POST("user/auth/login")
    suspend fun login(
        @Field(ApiConst.Query.PHONE) phone: String,
    ): MABaseResponse<Any>

    @FormUrlEncoded
    @POST("provider/auth/login")
    suspend fun loginProvider(
        @Field(ApiConst.Query.PHONE) phone: String,
        @Field(ApiConst.Query.PASSWORD) password: String,
    ): MABaseResponse<ProviderData>
    /*
    {
    "code": 200,
    "message": "Verified successfully.",
    "data": {
        "id": 30,
        "account_type": "provider",
        "verified": 1,
        "name": "aaa",
        "email": null,
        "phone": "444",
        "latitude": null,
        "longitude": null,
        "address": "3 هاشم الأشقر، الهايكستب، قسم النزهة، محافظة القاهرة‬ 11511",
        "link_unique_id": "#628a446da32bf",
        "token": "57|klIJPFWLEeoGBmJDTRSaaEBRa0p9aMvamE2sJMeq",
        "image": "https://hassan.my-staff.net/storage//tmp/phpBbmN97",
        "orders_flag": 0
    }
}
     */

    @FormUrlEncoded
    @POST("user/auth/update-profile")
    suspend fun updateUserProfile(
        @FieldMap map: Map<String, String>,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<ResponseUpdateProfile>
    /* no need for response
    {
        "code": 200,
        "message": "The action ran successfully!",
        "data": {
            "id": 7,
            "account_type": "user",
            "verified": 1,
            "name": "A",
            "email": null,
            "phone": "1234",
            "latitude": null,
            "longitude": null,
            "address": null,
            "link_unique_id": "#6278e5560cd9f",
            "token": null
        }
    }
     */

    @FormUrlEncoded
    @POST("provider/auth/update-profile")
    suspend fun updateProviderProfile(
        @Field(ApiConst.Query.LATITUDE) latitude: String,
        @Field(ApiConst.Query.LONGITUDE) longitude: String,
        @Field(ApiConst.Query.ADDRESS) address: String,
        @HeaderMap headerMap: Map<String, String>,
        @Field(ApiConst.Query.ACCOUNT_TYPE) accountType: String = ApiConst.Query.PROVIDER,
    ): MABaseResponse<Any>

    @FormUrlEncoded
    @POST("provider/auth/update-profile")
    suspend fun toggleCanReceiveOrders(
        @Field(ApiConst.Query.ORDERS_FLAG) ordersFlag: Int, // 0 means can get orders, so only 1 means get hours isa.
        @FieldMap queryMap: Map<String, String>, // (ApiConst.Query.HOURS) hours: Int
        @HeaderMap headerMap: Map<String, String>,
        @Field(ApiConst.Query.ACCOUNT_TYPE) accountType: String = ApiConst.Query.PROVIDER,
    ): MABaseResponse<Any>

    @Multipart
    @POST("user/auth/update-profile")
    suspend fun updateUserProfile(
        @Part image: MultipartBody.Part,
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<ResponseUpdateProfile>

    @Multipart
    @POST("provider/auth/register")
    suspend fun registerProvider(
        @Part image: MultipartBody.Part,
        @Part(ApiConst.Query.NAME) name: RequestBody,
        @Part(ApiConst.Query.PHONE) phone: RequestBody,
        @Part(ApiConst.Query.ADDRESS) address: RequestBody,
        @Part(ApiConst.Query.RELATIVE_PHONE) relativePhone: RequestBody,
        @Part(ApiConst.Query.CATEGORY_ID) categoryId: Int,
        @Part(ApiConst.Query.SERVICES + "[]") services: List<Int>,
        @Part imageFrontId: MultipartBody.Part,
        @Part imageBackId: MultipartBody.Part,
        @Part(ApiConst.Query.BIRTH_DATE) birthDate: RequestBody,
        @Part(ApiConst.Query.PASSWORD) password: RequestBody,
        @Part(ApiConst.Query.PASSWORD_CONFIRMATION) passwordConfirmation: RequestBody,
        @Part list: List<@JvmSuppressWildcards MultipartBody.Part>,
    ): MABaseResponse<ResponseUpdateProfile>

    @Multipart
    @POST("user/auth/update-profile")
    suspend fun updateUserProfile2(
        @PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
        @HeaderMap headerMap: Map<String, String>,
    ): MABaseResponse<ResponseUpdateProfile>

    @FormUrlEncoded
    @POST("user/auth/social-login")
    suspend fun socialLogin(
        @Field(ApiConst.Query.SOCIAL_ID) socialId: String,
        @Field(ApiConst.Query.PHONE) phone: String,
    ): MABaseResponse<ResponseVerifyCode>

    @FormUrlEncoded
    @POST("user/auth/social-login")
    suspend fun checkSocialLogin(
        @Field(ApiConst.Query.SOCIAL_ID) socialId: String,
    ): MABaseResponse<ResponseVerifyCode>

}