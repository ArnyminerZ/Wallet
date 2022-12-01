package com.arnyminerz.wallet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arnyminerz.wallet.data.`object`.FireflyAccount
import com.arnyminerz.wallet.data.`object`.FireflyCategory
import com.arnyminerz.wallet.data.`object`.FireflyCurrency

@Database(
    entities = [FireflyAccount::class, FireflyCurrency::class, FireflyCategory::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context, AppDatabase::class.java, "wallet-database")
                .build().also { INSTANCE = it }
        }
    }

    abstract fun accountsDao(): AccountsDao

    abstract fun currenciesDao(): CurrenciesDao

    abstract fun categoriesDao(): CategoriesDao
}
