package com.arnyminerz.wallet.database.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.arnyminerz.wallet.database.data.FireflyCategory

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
