package com.maproductions.mohamedalaa.shared.domain.orders

import com.google.gson.annotations.SerializedName
import com.maproductions.mohamedalaa.shared.core.customTypes.MABasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.TimeInDay
import com.maproductions.mohamedalaa.shared.core.extensions.minLengthOrPrefixZeros
import timber.log.Timber
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toKotlinDuration

data class ResponseListOfOrders(
    @SerializedName("orders_flag") var ordersFlag: Int?,
    /** Format 2022-06-19 18:54:31 */
    @SerializedName("orders_count_down") var ordersCountDown: String?,
    var approved: Int?,
    @SerializedName("on_the_way_orders") var onTheWayOrders: List<ResponseOrder>?,
    var orders: MABasePaging<ResponseOrder>?,
) {

    val stopReceivingOrders get() = ordersFlag == 1

    val isApproved get() = approved == 1

    val isSuspendedAccount get() = approved == 2

    fun getHoursAndMinutesAndSeconds(): Triple<Long, Long, Long>? {
        val (date, time) = ordersCountDown?.split(" ")?.let {
            if (it.size != 2) null else it.first() to it[1]
        } ?: return null

        val (year, month, day) = date.split("-").let {
            if (it.size != 3) null else Triple(
                it[0].toIntOrNull() ?: return null,
                it[1].toIntOrNull() ?: return null,
                it[2].toIntOrNull() ?: return null
            )
        } ?: return null

        val (hours24, minutes, seconds) = time.split(":").let {
            if (it.size != 3) null else Triple(
                it[0].toIntOrNull() ?: return null,
                it[1].toIntOrNull() ?: return null,
                it[2].toIntOrNull() ?: return null
            )
        } ?: return null

        val future = kotlin.runCatching {
            LocalDateTime.of(year, month, day, hours24, minutes, seconds)
        }.getOrElse {
            Timber.e("API ERROR IN FORMAT ordersCountDown $ordersCountDown\nError is $it")

            return null
        }

        val now = LocalDateTime.now()

        if (future.isAfter(now).not()) {
            return null
        }

        val duration = Duration.between(now, future).abs().toKotlinDuration()

        return Triple(
            duration.inWholeHours,
            duration.inWholeMinutes % 1.hours.inWholeMinutes,
            duration.inWholeSeconds % 1.minutes.inWholeSeconds
        )
    }

}
