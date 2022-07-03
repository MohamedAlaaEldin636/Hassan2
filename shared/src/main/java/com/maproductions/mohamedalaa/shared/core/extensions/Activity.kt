package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.maproductions.mohamedalaa.shared.R
import java.security.acl.AclNotFoundException

fun FragmentActivity.showAlertDialog(
    title: String,
    message: String,
    onDismissListener: () -> Unit = {},
    onPositiveButtonClick: () -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
            getString(R.string.ok)
        ) { _, _ ->
            onPositiveButtonClick()
        }
        .setOnDismissListener {
            onDismissListener()
        }
        .create()
        .show()
}

fun FragmentActivity.showAlertDialog(
    title: String,
    message: String,
    positiveText: CharSequence,
    negativeText: CharSequence,
    onPositiveButtonClick: (DialogInterface) -> Unit,
    onNegativeButtonClick: (DialogInterface) -> Unit,
    onDismissListener: () -> Unit = {},
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, _ ->
            onPositiveButtonClick(dialog)
        }
        .setNegativeButton(negativeText) { dialog, _ ->
            onNegativeButtonClick(dialog)
        }
        .setOnDismissListener {
            onDismissListener()
        }
        .create()
        .show()
}

fun <I> ActivityResultLauncher<I>.launchSafely(context: Context?, input: I) {
    try {
        launch(input)
    }catch (exception: ActivityNotFoundException) {
        context?.showErrorToast(context.getString(R.string.no_app_can_handle_this_action))
    }
}
