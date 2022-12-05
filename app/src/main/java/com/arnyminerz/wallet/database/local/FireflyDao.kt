package com.arnyminerz.wallet.database.local

import androidx.lifecycle.LiveData
import com.arnyminerz.wallet.database.data.FireflyObject

interface FireflyDao <T: FireflyObject> {
    fun getAll(): List<T>

    fun addAll(vararg values: T)

    fun updateAll(vararg values: T)

    fun deleteAll(vararg values: T)

    fun getAllLive(): LiveData<List<T>>
}
