package com.arnyminerz.wallet.utils

import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> ViewModel.async(@WorkerThread block: suspend CoroutineScope.() -> T) = viewModelScope.launch {
    withContext(Dispatchers.IO, block)
}
