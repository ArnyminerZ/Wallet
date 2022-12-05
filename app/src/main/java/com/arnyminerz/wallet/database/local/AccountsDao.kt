package com.arnyminerz.wallet.database.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.arnyminerz.wallet.database.data.FireflyAccount

@Dao
interface AccountsDao: FireflyDao<FireflyAccount> {
    @Query("SELECT * FROM ff_accounts")
    override fun getAll(): List<FireflyAccount>

    @Insert
    override fun addAll(vararg values: FireflyAccount)

    @Update
    override fun updateAll(vararg values: FireflyAccount)

    @Delete
    override fun deleteAll(vararg values: FireflyAccount)

    @Query("SELECT * FROM ff_accounts")
    override fun getAllLive(): LiveData<List<FireflyAccount>>
}
