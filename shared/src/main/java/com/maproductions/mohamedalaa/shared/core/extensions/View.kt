package com.maproductions.mohamedalaa.shared.core.extensions

import android.view.View
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.core.di.module.GsonModule

/**
 * @param id if `null` then [View.setTag] without id will be used else the other one will be used isa.
 */
inline fun <reified T> View.setTagViaGson(value: T, gson: Gson = GsonModule.provideGson(), id: Int? = null) {
	val json = kotlin.runCatching { gson.toJson(value, T::class.java) }.getOrNull()

	if (id != null) {
		setTag(id, json)
	}else{
		tag = json
	}
}

/**
 * @param id if `null` then [View.setTag] without id will be used else the other one will be used isa.
 */
inline fun <reified T> View.getTagViaGson(gson: Gson = GsonModule.provideGson(), id: Int? = null): T? {
	val json = (if (id != null) getTag(id) else tag) as? String ?: return null
	
	return kotlin.runCatching { gson.fromJson(json, T::class.java) }.getOrNull()
}
