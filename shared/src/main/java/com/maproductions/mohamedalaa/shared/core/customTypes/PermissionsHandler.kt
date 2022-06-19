package com.maproductions.mohamedalaa.shared.core.customTypes

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.launchSafely
import com.maproductions.mohamedalaa.shared.core.extensions.showAlertDialog
import com.maproductions.mohamedalaa.shared.core.extensions.showErrorToast
import java.lang.ref.WeakReference

/**
 * @param onSomePermissionsGranted if `null` then treat granting some permissions as all not granted.
 */
class PermissionsHandler(
    host: Any,
    listener: Listener,
    private val permissions: List<String>,
    private val onSomePermissionsGranted: ((permissions: Map<String, Boolean>) -> Unit)? = null,
    private val onPermissionsGranted: () -> Unit
) {

    private val weakRefHost = WeakReference(host)
    private val weakRefListener = WeakReference(listener)

    private val activityResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val activity = getActivityOrNull()

        when {
            permissions.values.toList().all { it == true } -> {
                onPermissionsGranted()
            }
            onSomePermissionsGranted != null && permissions.values.toList().any { it == true } -> {
                onSomePermissionsGranted.invoke(permissions)
            }
            // todo in the call ta7t bs el should show el mafrod le kollo w2 m4 le el location only see kda isa.
            activity != null && (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                    || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) -> {
                activity.showAlertDialog(
                    activity.getString(R.string.allow_location_permission),
                    activity.getString(R.string.this_app_need_allow_location),
                    onDismissListener = {
                        activity.showErrorToast(activity.getString(R.string.you_didn_t_accept_permission))
                    }
                ) {
                    startGrantingPermissions()
                }
            }
            else -> {
                weakRefListener.get()?.onDenyLocationPermissions(this)
            }
        }
    }

    private val activityResultPermissionsSystemSettings = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        executeActionOrAskForPermissions()
    }

    private val context: Context? get() {
        return when (val host = weakRefHost.get()) {
            is Fragment -> host.context
            is AppCompatActivity -> host
            else -> throw RuntimeException("Unexpected host to get context, host -> $host")
        }
    }

    fun executeActionOrAskForPermissions() {
        if (arePermissionsGranted()) {
            onPermissionsGranted()
        }else {
            startGrantingPermissions()
        }
    }

    fun arePermissionsGranted(): Boolean {
        val context = context ?: return false

        return permissions.firstOrNull {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        } != null
    }

    fun startGrantingPermissions() {
        activityResult.launchSafely(
            context,
            permissions.toTypedArray()
        )
    }

    private fun <I, O> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return when (val host = weakRefHost.get()) {
            is Fragment -> {
                host.registerForActivityResult(contract, callback)
            }
            is AppCompatActivity -> {
                host.registerForActivityResult(contract, callback)
            }
            else -> throw RuntimeException("Unexpected host $host")
        }
    }

    private fun shouldShowRequestPermissionRationale(permission: String): Boolean = when (val host = weakRefHost.get()) {
        is Fragment -> host.shouldShowRequestPermissionRationale(permission)
        is AppCompatActivity -> host.shouldShowRequestPermissionRationale(permission)
        else -> false
    }

    private fun getActivityOrNull() = when (val value = weakRefHost.get()) {
        is Fragment -> value.activity
        is AppCompatActivity -> value
        else -> null
    }

    interface Listener {
        fun onDenyLocationPermissions(permissionsHandler: PermissionsHandler) {
            val context = permissionsHandler.context

            permissionsHandler.getActivityOrNull()?.apply {
                showAlertDialog(
                    getString(R.string.change_permission_in_settings_of_device),
                    getString(R.string.this_app_need_allow_location),
                    onDismissListener = {
                        context?.showErrorToast(context.getString(R.string.you_didn_t_accept_permission))
                    }
                ) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                        it.data = Uri.fromParts("package", packageName, null)
                    }

                    permissionsHandler.activityResultPermissionsSystemSettings.launchSafely(
                        permissionsHandler.context,
                        intent
                    )
                }
            }
        }
    }

}
