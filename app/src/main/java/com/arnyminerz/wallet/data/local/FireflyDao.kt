package com.arnyminerz.wallet.data.local

import androidx.lifecycle.LiveData
import com.arnyminerz.wallet.data.`object`.FireflyObject

interface FireflyDao <T: FireflyObject> {
    fun getAll(): List<T>

    fun addAll(vararg values: T)

    fun updateAll(vararg values: T)

    fun deleteAll(vararg values: T)

    fun getAllLive(): LiveData<List<T>>
}
