package com.arnyminerz.wallet.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arnyminerz.wallet.BuildConfig
import com.arnyminerz.wallet.pkpass.data.Pass

@Database(entities = [Pass::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context) = instance ?: synchronized(AppDatabase) {
            instance ?: Room.databaseBuilder(context, AppDatabase::class.java, BuildConfig.APPLICATION_ID)
                .build()
                .also { instance = it }
        }
    }

    abstract fun passesDao(): PassesDao
}
