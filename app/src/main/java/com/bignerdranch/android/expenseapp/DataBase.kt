package com.bignerdranch.android.expenseappÏ

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.expenseapp.DateUpdate
import com.bignerdranch.android.expenseapp.Expense
import com.bignerdranch.android.expenseapp.Query

@Database(entities = [Expense::class], version = 2, exportSchema = false)
@TypeConverters(DateUpdate::class)
abstract class DataBase : RoomDatabase() {
    abstract fun Query(): Query
    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null
        fun getDatabase(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
