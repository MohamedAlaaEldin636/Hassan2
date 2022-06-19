package com.maproductions.mohamedalaa.shared.core.di.module

import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PusherModule {

    @Singleton
    @Provides
    fun providePusher(): Pusher {
        val options = PusherOptions()
        options.setCluster(PusherUtils.CHANNELS_CLUSTER)

        val pusher = Pusher(PusherUtils.CHANNELS_APP_KEY, options)
        pusher.connect()

        return pusher
    }

}