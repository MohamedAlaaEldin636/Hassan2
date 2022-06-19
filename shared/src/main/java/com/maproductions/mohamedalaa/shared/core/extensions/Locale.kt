package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.Context

private const val SHARED_PREF_LOCALE_FILE_NAME = "SHARED_PREF_LOCALE_FILE_NAME"
private const val SHARED_PREF_KEY_LOCALE = "SHARED_PREF_KEY_LOCALE"

fun Context.getProjectCurrentLocale(): String {
	val sharedPrefs = getSharedPreferences(SHARED_PREF_LOCALE_FILE_NAME, Context.MODE_PRIVATE)
	
	return sharedPrefs.getString(SHARED_PREF_KEY_LOCALE, "ar") ?: "ar"
}

fun Context.setProjectCurrentLocaleByToggling() {
	val current = getProjectCurrentLocale()
	
	val editor = getSharedPreferences(SHARED_PREF_LOCALE_FILE_NAME, Context.MODE_PRIVATE).edit()
	
	editor.putString(SHARED_PREF_KEY_LOCALE, if (current == "ar") "en" else "ar")
	
	editor.commit()
}
