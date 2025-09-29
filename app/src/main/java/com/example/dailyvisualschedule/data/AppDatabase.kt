package com.example.dailyvisualschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PersistentTodoItem::class, RedeemedRewardEntry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoItemDao(): TodoItemDao
    abstract fun redeemedRewardDao(): RedeemedRewardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "visual_schedule_database"
                )
                .build()
                INSTANCE = instance
                instance // Ensures the instance is the result of the synchronized block
            }
        }
    }
}
