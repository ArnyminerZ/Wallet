package com.arnyminerz.wallet.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.arnyminerz.wallet.pkpass.data.Pass

@Dao
interface PassesDao {
    @Query("SELECT * FROM passes")
    fun getAll(): LiveData<List<Pass>>

    @Insert
    fun insert(vararg passes: Pass)
}