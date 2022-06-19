package com.maproductions.mohamedalaa.shared.core.di.module

import com.maproductions.mohamedalaa.shared.data.auth.dataSource.remote.ApiAuthServices
import com.maproductions.mohamedalaa.shared.data.codes.dataSource.remote.ApiCodesServices
import com.maproductions.mohamedalaa.shared.data.conversations.dataSource.remote.ApiConversationsServices
import com.maproductions.mohamedalaa.shared.data.home.dataSource.remote.ApiHomeServices
import com.maproductions.mohamedalaa.shared.data.myAccount.dataSource.remote.ApiMyAccountServices
import com.maproductions.mohamedalaa.shared.data.orders.dataSource.remote.ApiOrderServices
import com.maproductions.mohamedalaa.shared.data.settings.dataSource.remote.ApiSettingsServices
import com.maproductions.mohamedalaa.shared.data.shared.dataSource.remote.ApiSharedServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkServicesModule {

    @Provides
    @Singleton
    fun provideApiSettingsServices(retrofit: Retrofit): ApiSettingsServices =
        retrofit.create(ApiSettingsServices::class.java)

    @Provides
    @Singleton
    fun provideApiCodesServices(retrofit: Retrofit): ApiCodesServices =
        retrofit.create(ApiCodesServices::class.java)

    @Provides
    @Singleton
    fun provideApiMyAccountServices(retrofit: Retrofit): ApiMyAccountServices =
        retrofit.create(ApiMyAccountServices::class.java)

    @Provides
    @Singleton
    fun provideApiAuthServices(retrofit: Retrofit): ApiAuthServices =
        retrofit.create(ApiAuthServices::class.java)

    @Provides
    @Singleton
    fun provideApiHomeServices(retrofit: Retrofit): ApiHomeServices =
        retrofit.create(ApiHomeServices::class.java)

    @Provides
    @Singleton
    fun provideApiSharedServices(retrofit: Retrofit): ApiSharedServices =
        retrofit.create(ApiSharedServices::class.java)

    @Provides
    @Singleton
    fun provideApiOrderServices(retrofit: Retrofit): ApiOrderServices =
        retrofit.create(ApiOrderServices::class.java)

    @Provides
    @Singleton
    fun provideApiConversationsServices(retrofit: Retrofit): ApiConversationsServices =
        retrofit.create(ApiConversationsServices::class.java)

}
