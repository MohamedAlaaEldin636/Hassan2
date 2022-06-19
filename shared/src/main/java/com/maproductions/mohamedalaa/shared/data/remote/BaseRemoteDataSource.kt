package com.maproductions.mohamedalaa.shared.data.remote

import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.customTypes.MAResult
import com.maproductions.mohamedalaa.shared.core.di.module.OkHttpModule
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

open class BaseRemoteDataSource {

    @Inject
    lateinit var prefsAccount: PrefsAccount

    protected fun getAuthorizationHeader(authToken: String): Map<String, String> {
        return mapOf(OkHttpModule.HEADER_KEY_AUTHORIZATION to authToken)
    }

    protected suspend fun getAuthorizationHeader(): Map<String, String> {
        return mapOf(OkHttpModule.HEADER_KEY_AUTHORIZATION to prefsAccount.getApiBearerToken().first()!!)
    }

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> MABaseResponse<T>
    ): MAResult.Immediate<MABaseResponse<T>> = withContext(Dispatchers.IO) {
        try {
            Timber.d("abc hiiiiiiiiiiiii")

            val response = apiCall()

            Timber.d("response $response")

            val errorStatus = when (response.code) {
                200 -> {
                    return@withContext MAResult.Success(response)
                }
                403 -> {
                    MAResult.Failure.Status.TOKEN_EXPIRED
                }
                /*405 -> {
                    // Not used in this project as always on login you should re-verify, and in
                    // case of social login check if has phone number if so then just ignore
                    // verification step
                    MAResult.Failure.Status.ACTIVATION_NOT_VERIFIED
                }*/
                401 -> {
                    MAResult.Failure.Status.ERROR
                }
                in 500 until 600 -> {
                    MAResult.Failure.Status.SERVER_ERROR
                }
                else -> {
                    MAResult.Failure.Status.OTHER
                }
            }

            MAResult.Failure(errorStatus, response.code, response.message)
        }catch (throwable: Throwable) {
            Timber.d("safeApiCall throwable $throwable\n${throwable.stackTrace.joinToString("\n")}")

            when (throwable) {
                is HttpException -> {
                    Timber.d("code ${throwable.code()}, msg ${throwable.message}, http status msg ${throwable.message()}")

                    val errorStatus = when (throwable.code()) {
                        in 400 until 500 -> MAResult.Failure.Status.ERROR
                        in 500 until 600 -> MAResult.Failure.Status.SERVER_ERROR
                        else -> MAResult.Failure.Status.OTHER
                    }

                    MAResult.Failure(errorStatus, throwable.code(), throwable.message())
                }
                is UnknownHostException/*, is SocketException*/, is ConnectException -> {
                    MAResult.Failure(MAResult.Failure.Status.NO_INTERNET, message = throwable.message)
                }
                else -> {
                    MAResult.Failure(MAResult.Failure.Status.OTHER, message = throwable.message)
                }
            }
        }
    }

}
