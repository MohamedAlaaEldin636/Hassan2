package com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.navigation.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServiceDateAndTimeSelectionFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServiceDateAndTimeSelectionFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServicesLocationSelectionFragmentDirections
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.core.extensions.dpToPx
import com.maproductions.mohamedalaa.shared.core.extensions.minLengthOrPrefixZeros
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import com.maproductions.mohamedalaa.shared.core.extensions.showErrorToast
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ServiceDateAndTimeSelectionViewModel @Inject constructor(
    application: Application,
    val args: ServiceDateAndTimeSelectionFragmentArgs
) : AndroidViewModel(application) {

    companion object {
        private const val DIALOG_DATE_PICKER_KEY = "DIALOG_DATE_PICKER_KEY"

        private const val DIALOG_TIME_PICKER_KEY = "DIALOG_TIME_PICKER_KEY"
    }

    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null

    private var hour24: Int? = null
    private var minute: Int? = null

    private val dp2AsPx by lazy {
        myApp.dpToPx(2f).roundToInt()
    }

    private val dp16AsPx by lazy {
        myApp.dpToPx(16f)
    }

    val currentSelectionIsDateAndTimeNotOrderNow = MutableLiveData(true)

    val strokeDateAndTime = currentSelectionIsDateAndTimeNotOrderNow.map {
        if (it == true) dp2AsPx else 0
    }
    val strokeOrderNow = currentSelectionIsDateAndTimeNotOrderNow.map {
        if (it != true) dp2AsPx else 0
    }
    val elevationDateAndTime = currentSelectionIsDateAndTimeNotOrderNow.map {
        if (it == true) dp16AsPx else 0f
    }
    val elevationOrderNow = currentSelectionIsDateAndTimeNotOrderNow.map {
        if (it != true) dp16AsPx else 0f
    }

    val date = MutableLiveData("")

    val time = MutableLiveData("")

    fun selectDateAndTime() {
        currentSelectionIsDateAndTimeNotOrderNow.value = true
    }

    fun orderNow() {
        currentSelectionIsDateAndTimeNotOrderNow.value = false
    }

    fun pickDate(view: View) {
        // Makes only dates from today forward selectable.
        val constraints = CalendarConstraints.Builder()
            .setValidator(
                DateValidatorPointForward.from(
                    LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli()
                )
            )
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(view.context.getString(SR.string.determine_the_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraints)
            .build()

        datePicker.addOnPositiveButtonClickListener { millis ->
            val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())

            val day = localDateTime.dayOfMonth
            val month = view.context.resources
                .getStringArray(SR.array.year_months)[localDateTime.monthValue.dec()]
            val year = localDateTime.year

            this.day = day
            this.month = localDateTime.monthValue
            this.year = year

            date.postValue("$day $month $year")
        }

        datePicker.show(view.findFragment<Fragment>().childFragmentManager, DIALOG_DATE_PICKER_KEY)
    }

    fun pickTime(view: View) {
        val now = LocalDateTime.now()

        val timePicker = MaterialTimePicker.Builder()
            .setTitleText(view.context.getString(SR.string.determine_the_time))
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(now.hour)
            .setMinute(now.minute)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val isAm = timePicker.hour < 12
            val hour = when {
                timePicker.hour == 0 -> 12
                timePicker.hour > 12 -> (timePicker.hour - 12)
                else -> timePicker.hour
            }

            this.hour24 = timePicker.hour
            this.minute = timePicker.minute

            val toggleText = if (isAm) {
                view.context.getString(SR.string.am_full)
            }else {
                view.context.getString(SR.string.pm_full)
            }
            time.postValue("${timePicker.minute} : $hour $toggleText")
        }

        timePicker.show(view.findFragment<Fragment>().childFragmentManager, DIALOG_TIME_PICKER_KEY)
    }

    fun next(view: View) {
        val (orderedAt, isUrgent) = if (currentSelectionIsDateAndTimeNotOrderNow.value == true) {
            val year = year
            val month = month
            val day = day

            val hour24 = hour24
            val minute = minute

            if (year != null && month != null && day != null && hour24 != null && minute != null) {
                val date = "$year-${month.minLengthOrPrefixZeros(2)}-${day.minLengthOrPrefixZeros(2)}"
                val time = "${hour24.minLengthOrPrefixZeros(2)}:${minute.minLengthOrPrefixZeros(2)}"

                "$date $time" to false
            }else {
                val msg = when {
                    year == null && hour24 == null -> view.context.getString(SR.string.you_must_select_date_and_time)
                    year == null -> view.context.getString(SR.string.you_must_select_date)
                    else -> view.context.getString(SR.string.you_must_select_time)
                }

                return view.context.showErrorToast(msg)
            }
        }else {
            val now = LocalDateTime.now()

            val date = "${now.year}-${now.monthValue.minLengthOrPrefixZeros(2)}-${now.dayOfMonth.minLengthOrPrefixZeros(2)}"
            val time = "${now.hour.minLengthOrPrefixZeros(2)}:${now.minute.minLengthOrPrefixZeros(2)}"

            "$date $time" to true
        }

        if (!isUrgent) {
            val now = LocalDateTime.now()

            val valid = day!! > now.dayOfMonth
                    || (day!! == now.dayOfMonth
                    && (now.hour.inc() < hour24!! || (now.hour.inc() == hour24!! && now.minute < minute!!)))
            if (!valid) {
                return myApp.showErrorToast(myApp.getString(SR.string.determination_min_one_hour))
            }
        }

        view.findNavController().navigate(
            ServiceDateAndTimeSelectionFragmentDirections.actionDestServiceDateAndTimeSelectionToDestServiceImageAndDescriptionSelection(
                args.categoryId,
                args.categoryName,
                args.jsonOfServices,
                args.addressId,
                orderedAt,
                if (isUrgent) ApiConst.Value.ORDER_TYPE_URGENT else ApiConst.Value.ORDER_TYPE_SCHEDULED,
                args.jsonDeliveryData
            )
        )
    }

}
