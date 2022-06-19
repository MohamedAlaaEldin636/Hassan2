package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.Context
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.TimeInDay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * 08 : 15 AM
 *
 */
fun LocalDateTime.formatTime1(context: Context): String = context.run {
    val timeInDay = TimeInDay(hour, minute)

    val hour = timeInDay.hour12.minLengthOrPrefixZeros(2)
    val min = timeInDay.minute.minLengthOrPrefixZeros(2)
    val dayNotation = if (timeInDay.isAm) getString(R.string.am_as_is) else getString(R.string.pm_as_is)

    "$hour : $min $dayNotation"
}

/**
 *
 */
fun LocalDateTime.formatDate1(context: Context): String = context.run {
    val dayName = format(DateTimeFormatter.ofPattern("eeee", Locale("ar")))

    val monthName = context.getMonthName(monthValue.dec())

    "$dayName - $monthName $year / $monthValue / $dayOfMonth"
}

/** format is Y-M-D H:M >>> hour is 24 */
fun String.convertToLocalDateTimeFromFormat1(): LocalDateTime {
    val (date, time) = split(" ").let { it[0] to it[1] }

    val (year, month, day) = date.split("-").let { Triple(it[0], it[1], it[2]) }
    val (hour24, minute) = time.split(":").let { it[0] to it[1] }

    return LocalDateTime.of(year.toInt(), month.toInt(), day.toInt(), hour24.toInt(), minute.toInt())
}
