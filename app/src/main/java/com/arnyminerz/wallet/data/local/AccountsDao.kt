package com.arnyminerz.wallet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.arnyminerz.wallet.data.`object`.FireflyAccount

@Dao
interface AccountsDao {
    @Query("SELECT * FROM ff_accounts")
    fun getAll(): List<FireflyAccount>

    @Insert
    fun addAll(vararg accounts: FireflyAccount)

    @Update
    fun updateAll(vararg account: FireflyAccount)

    @Delete
    fun deleteAll(vararg account: FireflyAccount)
}
