package com.maproductions.mohamedalaa.hassanu.core.di.module

import androidx.lifecycle.SavedStateHandle
import com.maproductions.mohamedalaa.hassanu.presentation.address.AddNewAddressFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.address.DelAddressCheckDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.address.DoneAddingAddressDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.auth.RegisterPhoneFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.global.RateProviderDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.home.HomeFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.order.FinishedOrderPaymentDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.order.OrderAdditionsFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.order.OrderDetailsFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.order.RateProvider2DialogFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.provider.ProviderDetailsFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.service.*
import com.maproductions.mohamedalaa.hassanu.presentation.settings.GetDiscountCodeDialogFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.settings.SettingsFragmentArgs
import com.maproductions.mohamedalaa.shared.core.extensions.asBundle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelArgsModule {

    @Provides
    fun providePendingProviderServiceRequestDialogFragmentArgs(state: SavedStateHandle): PendingProviderServiceRequestDialogFragmentArgs {
        return PendingProviderServiceRequestDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideServicesSelectionFragmentArgs(state: SavedStateHandle): ServicesSelectionFragmentArgs {
        return ServicesSelectionFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideAcceptedProviderServiceRequestDialogFragmentArgs(state: SavedStateHandle): AcceptedProviderServiceRequestDialogFragmentArgs {
        return AcceptedProviderServiceRequestDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideRateProviderDialogFragmentArgs(state: SavedStateHandle): RateProviderDialogFragmentArgs {
        return RateProviderDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideAddNewAddressFragmentArgs(state: SavedStateHandle): AddNewAddressFragmentArgs {
        return AddNewAddressFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideHomeFragmentArgs(state: SavedStateHandle): HomeFragmentArgs {
        return HomeFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideSettingsFragmentArgs(state: SavedStateHandle): SettingsFragmentArgs {
        return SettingsFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideDoneAddingAddressDialogFragmentArgs(state: SavedStateHandle): DoneAddingAddressDialogFragmentArgs {
        return DoneAddingAddressDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideDelAddressCheckDialogFragmentArgs(state: SavedStateHandle): DelAddressCheckDialogFragmentArgs {
        return DelAddressCheckDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideGetDiscountCodeDialogFragmentArgs(state: SavedStateHandle): GetDiscountCodeDialogFragmentArgs {
        return GetDiscountCodeDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideServicesLocationSelectionFragmentArgs(state: SavedStateHandle): ServicesLocationSelectionFragmentArgs {
        return ServicesLocationSelectionFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideServiceDateAndTimeSelectionFragmentArgs(state: SavedStateHandle): ServiceDateAndTimeSelectionFragmentArgs {
        return ServiceDateAndTimeSelectionFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideServiceImageAndDescriptionSelectionFragmentArgs(state: SavedStateHandle): ServiceImageAndDescriptionSelectionFragmentArgs {
        return ServiceImageAndDescriptionSelectionFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideServiceConfirmationFragmentArgs(state: SavedStateHandle): ServiceConfirmationFragmentArgs {
        return ServiceConfirmationFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideProviderDetailsFragmentArgs(state: SavedStateHandle): ProviderDetailsFragmentArgs {
        return ProviderDetailsFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideOrderDetailsFragmentArgs(state: SavedStateHandle): OrderDetailsFragmentArgs {
        return OrderDetailsFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideRateProvider2DialogFragmentArgs(state: SavedStateHandle): RateProvider2DialogFragmentArgs {
        return RateProvider2DialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideOrderAdditionsFragmentArgs(state: SavedStateHandle): OrderAdditionsFragmentArgs {
        return OrderAdditionsFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideFinishedOrderPaymentDialogFragmentArgs(state: SavedStateHandle): FinishedOrderPaymentDialogFragmentArgs {
        return FinishedOrderPaymentDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideRegisterPhoneFragmentArgs(state: SavedStateHandle): RegisterPhoneFragmentArgs {
        return RegisterPhoneFragmentArgs.fromBundle(state.asBundle())
    }

}
