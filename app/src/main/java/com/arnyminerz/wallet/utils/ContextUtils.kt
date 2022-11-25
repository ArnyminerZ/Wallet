package com.arnyminerz.wallet.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wallet")

/**
 * Gets a flow for the desired preference.
 * @author Arnau Mora
 * @since 20221114
 * @param key The key to get.
 * @param default The default value to return if there's no value stored at [key].
 * @return The value stored at [key], or [default] if none.
 */
fun <T> Context.getPreference(key: Preferences.Key<T>, default: T) = dataStore
    .data
    .map { it[key] ?: default }

/**
 * Gets a flow for the desired preference.
 * @author Arnau Mora
 * @since 20221114
 * @param key The key to get.
 * @return The value stored at [key], or null if none.
 */
fun <T> Context.getPreference(key: Preferences.Key<T>) = dataStore
    .data
    .map { it[key] }

/**
 * Returns the [AppCompatActivity] attached to the context.
 * @author Arnau Mora
 * @since 20221126
 */
val Context.activity: AppCompatActivity?
    get() = when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.activity
        else -> null
    }

@IntDef(Toast.LENGTH_SHORT, Toast.LENGTH_LONG)
annotation class ToastDuration

@UiThread
fun Context.toast(text: CharSequence, @ToastDuration duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, text, duration).show()

@UiThread
fun Context.toast(@StringRes textRes: Int, @ToastDuration duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, textRes, duration).show()
