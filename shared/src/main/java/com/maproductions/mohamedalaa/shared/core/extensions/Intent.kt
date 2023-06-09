package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.*
import android.net.Uri
import androidx.core.content.getSystemService
import com.maproductions.mohamedalaa.shared.R

fun Context.launchTelegram() {
	val intent = packageManager?.getLaunchIntentForPackage("org.telegram.messenger")
	
	if (intent == null) {
		showErrorToast(getString(R.string.app_not_found))
	}
	
	launchActivitySafely {
		if (intent != null) {
			startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
		}
	}
}

fun Context.launchDialNumber(phoneNumber: String) {
	val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber.trim()}"))
	
	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Context.launchWhatsApp(phoneNumber: String) {
	/*
	void openWhatsappContact(String number) {
    Uri uri = Uri.parse("smsto:" + number);
    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
    i.setPackage("com.whatsapp");
    startActivity(Intent.createChooser(i, ""));
}
	 */
	val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${phoneNumber.trim()}"))
	intent.`package` = "com.whatsapp"

	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Context.launchShareText(text: String) {
	val intent = Intent(Intent.ACTION_SEND).also {
		it.type = "text/plain"
		it.putExtra(Intent.EXTRA_TEXT, text)
	}
	
	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Context.copyToClipboard(userVisibleLabel: String, actualTextInClip: String = userVisibleLabel) {
	val clipboard = getSystemService<ClipboardManager>() ?: return

	val clip = ClipData.newPlainText(userVisibleLabel, actualTextInClip)
	clipboard.setPrimaryClip(clip)

	showSuccessToast(getString(R.string.copy_is_done_successfully))
}

fun Context.launchBrowser(link: String) {
	if (link.isEmpty()) {
		return
	}

	val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
	
	launchActivitySafely {
		startActivity(intent.wrapInChooser(getString(R.string.pick_app)))
	}
}

fun Intent.wrapInChooser(title: CharSequence): Intent {
	return Intent.createChooser(this, title)
}

fun Context.launchAppOnGooglePlay() {
	launchActivitySafely {
		try {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).wrapInChooser(getString(R.string.pick_app)))
		}catch (e: ActivityNotFoundException) {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getAppWebLinkOnGooglePay())).wrapInChooser(getString(R.string.pick_app)))
		}
	}
}

fun Context.launchProviderAppOnGooglePlay() {
	val packageName = "com.grand.hassan.provider2"
	launchActivitySafely {
		try {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).wrapInChooser(getString(R.string.pick_app)))
		}catch (e: ActivityNotFoundException) {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getAppWebLinkOnGooglePay())).wrapInChooser(getString(R.string.pick_app)))
		}
	}
}

fun Context.getAppWebLinkOnGooglePay(): String {
	return "https://play.google.com/store/apps/details?id=$packageName"
}

private fun Context.launchActivitySafely(msg: String = getString(R.string.something_went_wrong), block: () -> Unit) {
	try {
		block()
	}catch (throwable: Throwable) {
		showErrorToast(msg)
	}
}
