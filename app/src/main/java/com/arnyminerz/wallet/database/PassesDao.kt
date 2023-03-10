package com.arnyminerz.wallet.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.arnyminerz.wallet.pkpass.data.Pass

@Dao
interface PassesDao {
    @Query("SELECT * FROM passes")
    fun getAll(): LiveData<List<Pass>>

    @WorkerThread
    @Insert
    suspend fun insert(vararg passes: Pass)

    @WorkerThread
    @Delete
    suspend fun delete(vararg passes: Pass)

    @WorkerThread
    @Query("UPDATE passes SET archived=:archived WHERE id=:id")
    suspend fun archive(id: Long, archived: Boolean)
}