package com.maproductions.mohamedalaa.hassanp.core.di.module

import androidx.lifecycle.SavedStateHandle
import com.maproductions.mohamedalaa.hassanp.presentation.auth.NewPasswordFragmentArgs
import com.maproductions.mohamedalaa.hassanp.presentation.home.NewOrderDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.MyAccountFragmentArgs
import com.maproductions.mohamedalaa.hassanp.presentation.order.ConfirmFinishingWorkDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanp.presentation.order.MoneyReceivedDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanp.presentation.order.OrderDetailsFragmentArgs
import com.maproductions.mohamedalaa.hassanp.presentation.service.AddingServicesFragmentArgs
import com.maproductions.mohamedalaa.shared.core.extensions.asBundle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelArgsModule {

    @Provides
    fun provideMyAccountFragmentArgs(state: SavedStateHandle): MyAccountFragmentArgs {
        return MyAccountFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideOrderDetailsFragmentArgs(state: SavedStateHandle): OrderDetailsFragmentArgs {
        return OrderDetailsFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideNewOrderDialogFragmentArgs(state: SavedStateHandle): NewOrderDialogFragmentArgs {
        return NewOrderDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideNewPasswordFragmentArgs(state: SavedStateHandle): NewPasswordFragmentArgs {
        return NewPasswordFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideConfirmFinishingWorkDialogFragmentArgs(state: SavedStateHandle): ConfirmFinishingWorkDialogFragmentArgs {
        return ConfirmFinishingWorkDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideMoneyReceivedDialogFragmentArgs(state: SavedStateHandle): MoneyReceivedDialogFragmentArgs {
        return MoneyReceivedDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideAddingServicesFragmentArgs(state: SavedStateHandle): AddingServicesFragmentArgs {
        return AddingServicesFragmentArgs.fromBundle(state.asBundle())
    }

}