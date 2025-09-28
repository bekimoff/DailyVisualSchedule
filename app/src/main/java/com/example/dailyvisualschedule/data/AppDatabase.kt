package com.example.dailyvisualschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dailyvisualschedule.R // For default task icons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PersistentTodoItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoItemDao(): TodoItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "visual_schedule_database"
                )
                .addCallback(AppDatabaseCallback(scope, context)) // Add callback for pre-population
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context // Needed to access resources for default items
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch(Dispatchers.IO) {
                    populateDatabase(it.todoItemDao(), context)
                }
            }
        }

        suspend fun populateDatabase(todoItemDao: TodoItemDao, context: Context) {
            // Add default daytime tasks
            val dayTasks = listOf(
                PersistentTodoItem(taskName = "Use Toilet", imageResId = R.drawable.ic_toilet, scheduleType = "day"),
                PersistentTodoItem(taskName = "Brush Teeth", imageResId = R.drawable.ic_toothbrush, scheduleType = "day"),
                PersistentTodoItem(taskName = "Get Dressed", imageResId = R.drawable.ic_clothes, scheduleType = "day"),
                PersistentTodoItem(taskName = "Put on Shoes", imageResId = R.drawable.ic_shoes, scheduleType = "day"),
                PersistentTodoItem(taskName = "Take Backpack", imageResId = R.drawable.ic_backpack, scheduleType = "day")
            )
            todoItemDao.insertAll(dayTasks)

            // Add default nighttime tasks
            val nightTasks = listOf(
                PersistentTodoItem(taskName = "Use toilet", imageResId = R.drawable.ic_toilet, scheduleType = "night"),
                PersistentTodoItem(taskName = "Brush teeth", imageResId = R.drawable.ic_toothbrush, scheduleType = "night"),
                PersistentTodoItem(taskName = "Sweep floor", imageResId = R.drawable.ic_broom, scheduleType = "night"),
                PersistentTodoItem(taskName = "Read book", imageResId = R.drawable.ic_book, scheduleType = "night"),
                PersistentTodoItem(taskName = "Go to bed", imageResId = R.drawable.ic_pyjamas, scheduleType = "night")
            )
            todoItemDao.insertAll(nightTasks)
        }
    }
}
