package com.maproductions.mohamedalaa.shared.presentation.onBoard.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.customTypes.switchMapMultiple2
import com.maproductions.mohamedalaa.shared.core.extensions.defaultAnimationsNavOptionsBuilder
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsSplash
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.domain.settings.ItemOnBoard
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.maproductions.mohamedalaa.shared.presentation.onBoard.OnBoardFragment
import com.maproductions.mohamedalaa.shared.presentation.onBoard.adapters.SliderRVOnBoard
import com.maproductions.mohamedalaa.shared.presentation.onBoard.adapters.viewHolder.SliderVHOnBoard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val args: NavSharedArgs,
    repoSettings: RepoSettings,
    private val prefsSplash: PrefsSplash
) : ViewModel() {

    val adapterSliders = SliderRVOnBoard()

    val retryAbleFlowList = RetryAbleFlow {
        repoSettings.getOnBoardScreenFor(args.isUserNotProvider)
    }

    val currentPageIndex = MutableLiveData(0)

    val list = MutableLiveData(emptyList<ItemOnBoard>())

    val showSkipNotStartButtonText = switchMapMultiple2(list, currentPageIndex) {
        list.value.isNullOrEmpty() || currentPageIndex.value.orZero() < list.value.orEmpty().lastIndex
    }

    fun skipOrStartApplication(view: View) {
        // Just move to next one isa. (not in this app)
        //view.findFragment<OnBoardFragment>().binding?.sliderView?.slideToNextPosition()

        viewModelScope.launch {
            prefsSplash.setInitialLaunch(SplashInitialLaunch.LOGIN)

            view.findNavController().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.shared.log.in",
                defaultAnimationsNavOptionsBuilder()
                    .setPopUpTo(R.id.dest_on_board, true)
                    .build()
            )
        }
    }

}
