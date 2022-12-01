package com.arnyminerz.wallet.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.arnyminerz.wallet.data.`object`.FireflyAccount
import com.arnyminerz.wallet.data.`object`.FireflyCategory

@Dao
interface CategoriesDao: FireflyDao<FireflyCategory> {
    @Query("SELECT * FROM ff_categories")
    override fun getAll(): List<FireflyCategory>

    @Insert
    override fun addAll(vararg values: FireflyCategory)

    @Update
    override fun updateAll(vararg values: FireflyCategory)

    @Delete
    override fun deleteAll(vararg values: FireflyCategory)

    @Query("SELECT * FROM ff_categories")
    override fun getAllLive(): LiveData<List<FireflyCategory>>
}
