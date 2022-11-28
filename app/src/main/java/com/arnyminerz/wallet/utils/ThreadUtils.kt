package com.arnyminerz.wallet.utils

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

fun doAsync(@WorkerThread block: suspend CoroutineScope.() -> Unit) =
    CoroutineScope(Dispatchers.IO).launch { block(this) }

suspend fun ui(@UiThread block: suspend CoroutineScope.() -> Unit) =
    withContext(Dispatchers.Main) { block(this) }

/**
 * Launches the given block in the IO thread.
 * @author Arnau Mora
 * @since 20221128
 */
fun ViewModel.launch(@WorkerThread block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(Dispatchers.IO, block = block)

/**
 * Returns a [MutableLiveData] that gets updated with the values posted by [block].
 * @author Arnau Mora
 * @since 20221128
 * @param block The block of code to run for fetching values.
 */
fun <T> ViewModel.future(@WorkerThread block: suspend MutableLiveData<T?>.() -> Unit): MutableLiveData<T?> = MutableLiveData<T?>(null).apply {
    launch { block(this@apply) }
}
