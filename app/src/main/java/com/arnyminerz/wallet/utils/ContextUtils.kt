package com.arnyminerz.wallet.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.arnyminerz.wallet.utils.serializer.JsonSerializable
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import timber.log.Timber
import kotlin.reflect.KClass

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
 * Gets a flow for the desired preference.
 * @author Arnau Mora
 * @since 20221114
 * @param key The key to get.
 * @param default The default value to return if there's no value stored at [key].
 * @return The value stored at [key], or [default] if none.
 */
fun <T: JsonSerializable> Context.getPreference(key: Preferences.Key<Set<String>>, default: Set<String>, serializer: JsonSerializer<T>) = dataStore
    .data
    .map { it[key] ?: default }
    .map { list ->
        Timber.i("List: $list")
        list.map { JSONObject(it) }
            .map { serializer.fromJson(it) }
    }

/**
 * Updates the value of the given preference.
 * @author Arnau Mora
 * @since 20221126
 * @param key The key to store the value at.
 * @param value What to store at [key].
 */
suspend fun <T> Context.setPreference(key: Preferences.Key<T>, value: T) = dataStore
    .edit { it[key] = value }

/**
 * Removed the stored value for the given preference.
 * @author Arnau Mora
 * @since 20221126
 * @param key The key to store the value at.
 */
suspend fun <T> Context.popPreference(key: Preferences.Key<T>) = dataStore
    .edit { it.remove(key) }

/**
 * Adds the given value to the set in the [key]'s preference.
 * @author Arnau Mora
 * @since 20221126
 * @param key The key to store the value at.
 * @param values All the values to add.
 */
suspend fun Context.addToPreference(key: Preferences.Key<Set<String>>, vararg values: String) =
    getPreference(key, emptySet()).first().let { set ->
        val entries = set.toMutableSet()
        entries.addAll(values.toList())
        setPreference(key, entries)
    }

/**
 * Adds the given value to the set in the [key]'s preference.
 * @author Arnau Mora
 * @since 20221126
 * @param key The key to store the value at.
 * @param values All the values to add.
 */
suspend fun <T: JsonSerializable> Context.addToPreference(key: Preferences.Key<Set<String>>, vararg values: T) =
    getPreference(key, emptySet()).first().let { set ->
        val entries = set.toMutableSet()
        entries.addAll(values.toList().map { it.toJson().toString() })
        setPreference(key, entries)
    }

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

/**
 * Starts the given [activity] using [Context.startActivity].
 * @author Arnau Mora
 * @since 20221126
 * @param activity The class of the activity to launch.
 * @param options Some options to apply to the launch intent. Adding extras, for example.
 */
@UiThread
fun <A: Activity> Context.launch(activity: KClass<A>, options: Intent.() -> Unit = {}) = startActivity(Intent(this, activity.java).apply(options))
