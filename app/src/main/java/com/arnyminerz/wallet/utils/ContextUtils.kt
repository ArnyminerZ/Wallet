package com.arnyminerz.wallet.utils

import android.content.Context
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
