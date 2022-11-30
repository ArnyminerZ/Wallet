package com.arnyminerz.wallet.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.arnyminerz.wallet.data.`object`.FireflyAccount
import com.arnyminerz.wallet.data.`object`.FireflyCurrency

@Dao
interface CurrenciesDao: FireflyDao<FireflyCurrency> {
    @Query("SELECT * FROM ff_currencies")
    override fun getAll(): List<FireflyCurrency>

    @Insert
    override fun addAll(vararg values: FireflyCurrency)

    @Update
    override fun updateAll(vararg values: FireflyCurrency)

    @Delete
    override fun deleteAll(vararg values: FireflyCurrency)

    @Query("SELECT * FROM ff_currencies")
    override fun getAllLive(): LiveData<List<FireflyCurrency>>
}
