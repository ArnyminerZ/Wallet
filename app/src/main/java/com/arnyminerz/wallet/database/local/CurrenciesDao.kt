package com.arnyminerz.wallet.database.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.arnyminerz.wallet.database.data.FireflyCurrency

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
