package com.maproductions.mohamedalaa.shared.core.di.module

import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.core.customTypes.StaticValues
import com.maproductions.mohamedalaa.shared.core.extensions.asBundle
import com.maproductions.mohamedalaa.shared.presentation.auth.VerifyPhoneFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.chat.ChatDetailsFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.location.LocationTrackingFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.location.LocationViewerFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.order.CancelOrderDialogFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.order.CancellationReasonDialogFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.search.SearchQueriesFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.settings.ImageWithTextAndTitleFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.settings.MSGFormFragmentArgs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelArgsModule {

    @Provides
    fun provideCancellationReasonDialogFragmentArgs(state: SavedStateHandle): CancellationReasonDialogFragmentArgs {
        return CancellationReasonDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideNavSharedArgs(): NavSharedArgs {
        return NavSharedArgs.fromBundle(bundleOf(
            "isUserNotProvider" to StaticValues.isUserNotProvider
        ))
    }

    @Provides
    fun provideLocationSelectionFragmentArgs(state: SavedStateHandle): LocationSelectionFragmentArgs {
        return LocationSelectionFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideLocationViewerFragmentArgs(state: SavedStateHandle): LocationViewerFragmentArgs {
        return LocationViewerFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideLocationTrackingFragmentArgs(state: SavedStateHandle): LocationTrackingFragmentArgs {
        return LocationTrackingFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideImageWithTextAndTitleFragmentArgs(state: SavedStateHandle): ImageWithTextAndTitleFragmentArgs {
        return ImageWithTextAndTitleFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideChatDetailsFragmentArgs(state: SavedStateHandle): ChatDetailsFragmentArgs {
        return ChatDetailsFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideVerifyPhoneFragmentArgs(state: SavedStateHandle): VerifyPhoneFragmentArgs {
        return VerifyPhoneFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideSearchQueriesFragmentArgs(state: SavedStateHandle): SearchQueriesFragmentArgs {
        return SearchQueriesFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideMSGFormFragmentArgs(state: SavedStateHandle): MSGFormFragmentArgs {
        return MSGFormFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideCancelOrderDialogFragmentArgs(state: SavedStateHandle): CancelOrderDialogFragmentArgs {
        return CancelOrderDialogFragmentArgs.fromBundle(state.asBundle())
    }

}
