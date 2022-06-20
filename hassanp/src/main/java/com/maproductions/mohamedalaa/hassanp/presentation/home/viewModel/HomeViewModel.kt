@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("SameParameterValue")

package com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel

import android.app.Application
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.*
import com.maproductions.mohamedalaa.hassanp.presentation.home.HomeFragment
import com.maproductions.mohamedalaa.hassanp.presentation.home.adapters.RVItemOrderInHome
import com.maproductions.mohamedalaa.shared.core.customTypes.OrdersCategory
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.domain.search.SearchType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    prefsAccount: PrefsAccount,
    repoOrder: RepoOrder,
    private val repoAuth: RepoAuth,
) : AndroidViewModel(application) {

    private var timer: CountDownTimer? = null

    private val providerData = prefsAccount.getProviderData()

    private val name = providerData.mapLatest { it?.name }.asLiveData()

    val search = MutableLiveData("")

    val retryAbleFlowAllStatuses = RetryAbleFlow(repoOrder::getAllStatuses)

    val orders = search.asFlow().flatMapLatest {
        repoOrder.getOrdersForProvider(
            OrdersCategory.PENDING,
            it.orEmpty(),
        )
    }

    val adapter = RVItemOrderInHome(repoOrder)

    val greetingText = name.map {
        val res = if (LocalTime.now().hour < 12) SR.string.good_morning_var else SR.string.good_evening_var

        myApp.getString(res, it.orEmpty())
    }

    /** Ex. -> ( 04 : 45 : 12 ) */
    val timeToReceiveOrdersAfter = MutableLiveData("")

    fun clearSearchFilter() = View.OnClickListener {
        search.value = ""
    }

    fun toSearchQueries(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.search.queries.with.orders.category",
            paths = arrayOf(SearchType.PROVIDER_HOME.name, OrdersCategory.PENDING.name)
        )
    }

    fun toggleCanReceiveOrders(view: View) {
        if (timeToReceiveOrdersAfter.value.isNullOrEmpty()) {
            view.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                "dialog-dest",
                "com.grand.hassan.shared.stop.recieving.orders.dialog"
            )
        }else {
            // Start receiving order isa.

            val fragment = view.findFragment<HomeFragment>()

            fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
                afterShowingLoading = {
                    repoAuth.startReceiveOrders()
                },
                afterHidingLoading = {
                    timer?.cancel()

                    timeToReceiveOrdersAfter.value = ""
                }
            )
        }
    }

    /** @param hours `0` means until i turn it back on isa. */
    fun stopReceivingOrders(hours: Long, minutes: Long = 0L, seconds: Long = 0L) {
        if (hours == 0L && minutes == 0L && seconds == 0L) {
            timer?.cancel()

            timeToReceiveOrdersAfter.value = "   "
        }else {
            initiateTimer(hours, minutes, seconds)
        }
    }

    private fun initiateTimer(hours: Long, minutes: Long, seconds: Long) {
        timer?.cancel()

        val millis = hours.hours.inWholeMilliseconds.plus(
            minutes.minutes.inWholeMilliseconds
        ).plus(
            seconds.seconds.inWholeMilliseconds
        )

        timer = object : CountDownTimer(millis, TimeUnit.SECONDS.toMillis(1)) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished.milliseconds.inWholeSeconds == 0L) {
                    timeToReceiveOrdersAfter.value = ""

                    return
                }

                val duration = millisUntilFinished.milliseconds

                val totalHours = duration.inWholeHours.toInt().minLengthOrPrefixZeros(2)
                val totalMinutes = (duration.inWholeMinutes % 1.hours.inWholeMinutes).toInt()
                    .minLengthOrPrefixZeros(2)
                val totalSeconds = (duration.inWholeSeconds % 1.minutes.inWholeSeconds).toInt()
                    .minLengthOrPrefixZeros(2)

                // ( 04 : 45 : 12 )
                timeToReceiveOrdersAfter.value = "( $totalHours : $totalMinutes : $totalSeconds )"
            }

            override fun onFinish() {
                timeToReceiveOrdersAfter.value = ""
            }
        }

        timer?.start()
    }

    override fun onCleared() {
        timer?.cancel()

        super.onCleared()
    }

}
