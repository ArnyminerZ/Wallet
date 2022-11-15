package com.arnyminerz.wallet.utils

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun doAsync(@WorkerThread block: suspend CoroutineScope.() -> Unit) =
    CoroutineScope(Dispatchers.IO).launch { block(this) }

suspend fun ui(@UiThread block: suspend CoroutineScope.() -> Unit) =
    withContext(Dispatchers.Main) { block(this) }
