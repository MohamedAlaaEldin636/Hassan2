package com.maproductions.mohamedalaa.shared.core.extensions

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.shared.presentation.auth.VerifyPhoneFragment
import timber.log.Timber

/**
 * - Constructs a [Bundle] for every key/value pair in the `receiver` isa.
 */
fun SavedStateHandle.asBundle(): Bundle {
    val bundle = Bundle()
    for (key in keys()) {
        bundle.addValue(key, get(key))
    }

    return bundle
}

fun <T> SavedStateHandle.actOnGetIfNotInitialValueOrGetLiveData(
    key: String,
    initialValue: T,
    owner: LifecycleOwner,
    conditionOnResultToSetBackToInitialValue: (T?) -> Boolean,
    actionWhenConditionIsMet: (T?) -> Unit
) {
    val currentValue = get<T>(key)

    if (key == "DelAddressCheckDialogFragment.KEY_FRAGMENT_DELETION_ID") {
        Timber.e("abcdef has key ${contains(key)}")
    }

    if (contains(key) && currentValue != initialValue) {
        remove<T>(key)

        actionWhenConditionIsMet(currentValue)
    }else {
        getLiveData(
            key,
            initialValue
        ).observe(owner) {
            if (key == "DelAddressCheckDialogFragment.KEY_FRAGMENT_DELETION_ID") {
                Timber.e("abcdef entered key condition ${conditionOnResultToSetBackToInitialValue(it)} === $it")
            }

            if (conditionOnResultToSetBackToInitialValue(it)) {
                remove<T>(key)

                actionWhenConditionIsMet(it)
            }
        }
    }
}

fun <T> SavedStateHandle.actOnGetLiveDataToResetKey(
    key: String,
    initialValue: T,
    owner: LifecycleOwner,
    conditionOnResultToSetBackToInitialValue: (T?) -> Boolean,
    actionWhenConditionIsMet: (T?) -> Unit
) {
    getLiveData(
        key,
        initialValue
    ).observe(owner) {
        if (conditionOnResultToSetBackToInitialValue(it)) {
            set<T>(key, initialValue)

            actionWhenConditionIsMet(it)
        }
    }
}

fun <T> SavedStateHandle.actOnGetLiveDataToRemoveKey(
    key: String,
    initialValue: T,
    owner: LifecycleOwner,
    conditionOnResultToSetBackToInitialValue: (T?) -> Boolean,
    actionWhenConditionIsMet: (T?) -> Unit
) {
    val observer = object : Observer<T> {
        override fun onChanged(it: T) {
            if (conditionOnResultToSetBackToInitialValue(it)) {
                remove<T>(key)

                actionWhenConditionIsMet(it)

                getLiveData(
                    key,
                    initialValue
                ).observe(owner, this)
            }
        }
    }

    getLiveData(
        key,
        initialValue
    ).observe(owner, observer)
}


