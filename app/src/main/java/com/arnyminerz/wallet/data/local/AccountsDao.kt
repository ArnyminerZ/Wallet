package com.arnyminerz.wallet.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.arnyminerz.wallet.data.`object`.FireflyAccount

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
