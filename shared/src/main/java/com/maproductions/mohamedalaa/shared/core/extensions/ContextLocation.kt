package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseActivity
import timber.log.Timber
import java.util.*

@WorkerThread
fun Context.getAddressFromLatitudeAndLongitude(
    latitude: Double,
    longitude: Double,
    fallbackAddress: String = getString(R.string.your_address_has_been_selected_successfully),
): String {
    val address = try {
        val geocoder = Geocoder(this, Locale(MABaseActivity.APPLICATION_DEFAULT_LANGUAGE))
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses.isNotEmpty()) {
            addresses[0].getAddressLine(0).also {
                Timber.d(it)
            }
        }else {
            Timber.d("address NULL")

            null
        }
    }catch (throwable: Throwable) {
        Timber.e(throwable)

        null
    }

    return address ?: fallbackAddress
}

fun Context.checkSelfPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
